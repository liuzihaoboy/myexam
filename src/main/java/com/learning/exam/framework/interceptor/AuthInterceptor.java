package com.learning.exam.framework.interceptor;

import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.enums.converter.RoleEnumConverter;
import com.learning.exam.framework.exception.AuthException;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.service.impl.RedisServiceImpl;
import com.learning.exam.model.entity.TbPermission;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author liu_yeye
 * @date 2018-05-21 11:33
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("开始执行权限拦截器");
        String uri = httpServletRequest.getRequestURI();
        log.info("访问url={}",uri);
        HttpSession session = httpServletRequest.getSession();
        RedisService redisService = SpringBeanUtils.getBean(RedisServiceImpl.class);
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        if(tbUserVo==null){
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/login");
            return false;
        }else {
            String loginSessionId = redisService.get(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),String.class);
            //旧的登录信息
            if(!loginSessionId.equals(session.getId())){
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/login");
                return false;
            }
            //url权限
            if("/index".equals(uri)||"/index.html".equals(uri)){
                updateSessionTime(redisService,session.getId(),Integer.toString(tbUserVo.getId()));
                return true;
            }
            if(uri.startsWith("/common")){
                updateSessionTime(redisService,session.getId(),Integer.toString(tbUserVo.getId()));
                return true;
            }
            RoleEnum roleEnum = RoleEnumConverter.converter(tbUserVo.getTbRole().getId());
            log.info("登录角色role={}",roleEnum.getRoleName());
            //学生
            if(roleEnum == RoleEnum.Student&&uri.startsWith("/user")){
                updateSessionTime(redisService,session.getId(),Integer.toString(tbUserVo.getId()));
                return true;
                //其他
            }else if(uri.startsWith("/system")){
                String s = uri.replaceFirst("/system","");
                List<TbPermission> permissions = tbUserVo.getTbPermissions();
                int prefixIndex = s.indexOf((int)'/',1);
                String prefixStr = prefixIndex==-1?null:s.substring(0,prefixIndex);
                for(TbPermission tbPermission:permissions){
                    String perUrl = tbPermission.getPerUrl();
                    if("/*".equals(perUrl)){
                        updateSessionTime(redisService,session.getId(),Integer.toString(tbUserVo.getId()));
                        return true;
                    }else if(!"/".equals(perUrl)){
                        if(perUrl.equals(s)||
                                perUrl.equals(prefixStr)){
                            updateSessionTime(redisService,session.getId(),Integer.toString(tbUserVo.getId()));
                            return true;
                        }
                    }
                }
            }
            throw new AuthException(CodeMsg.AUTH_CHECK_ERROR);
        }
    }
    private void updateSessionTime(RedisService redisService,String sessionId,String userId){
        redisService.expire(SessionKey.sessionById,sessionId,SessionKey.sessionById.expireSeconds());
        redisService.expire(SessionKey.sessionByUserId,userId,SessionKey.sessionByUserId.expireSeconds());
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("结束执行权限拦截器");
    }
}
