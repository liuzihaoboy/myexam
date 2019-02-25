package com.learning.exam.controller;

import com.alibaba.fastjson.JSONObject;
import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.exception.AuthException;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.entity.TbPermission;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.vo.BaseMenu;
import com.learning.exam.model.vo.MenuVo;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.PaperService;
import com.learning.exam.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-03  16:27
 */
@Controller
@Slf4j
@RequestMapping(value = "/common")
public class CommonController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/head",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String head(HttpServletRequest request){
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        if(tbUserVo!=null){
            String title = "用户名:"+tbUserVo.getAccount()+" 姓名:"+tbUserVo.getName();
            String text = tbUserVo.getAccount()+"("+tbUserVo.getTbRole().getRoleName()+")";
            request.setAttribute("title",title);
            request.setAttribute("text",text);
        }
        return "head";
    }
    @RequestMapping(value = "/menu",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String menu(HttpServletRequest request){
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        if(tbUserVo!=null){
            if(RoleEnum.Student.getId().equals(tbUserVo.getTbRole().getId())){
                return "menu_student";
            }else if(RoleEnum.SuperAdmin.getId().equals(tbUserVo.getTbRole().getId())){
                return "menu_admin";
            }else {
                List<MenuVo> menuVos = menuVos(session.getId(),tbUserVo);
                request.setAttribute("menuVos",menuVos);
            }
        }
        return  "menu";
    }
    @RequestMapping(value = "/welcome",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String welcome(HttpServletRequest request){
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        if(RoleEnum.Student.getId().equals(tbUserVo.getTbRole().getId())){
            return "redirect:/user/index";
        }else if(RoleEnum.SuperAdmin.getId().equals(tbUserVo.getTbRole().getId())){
            return "welcome_admin";
        }else {
            List<MenuVo> menuVos = menuVos(session.getId(),tbUserVo);
            return "redirect:"+menuVos.get(0).getMenus().get(0).getUrl();
        }
    }
    private List<MenuVo> menuVos(String sessionId,TbUserVo tbUserVo){
        String menuVosJson = redisService.hget(SessionKey.sessionById,sessionId,SessionCacheName.BASE_MENU,String.class);
        List<MenuVo> menuVos = JSONObject.parseArray(menuVosJson,MenuVo.class);
        if(menuVos == null){
            List<TbPermission> permissions = tbUserVo.getTbPermissions();
            menuVos = new ArrayList<>();
            for (TbPermission tbPermission:permissions){
                String url = tbPermission.getPerUrl();
                if("/*".equals(url)||"/".equals(url)){
                    continue;
                }
                MenuVo menuVo = new MenuVo();
                menuVo.setTitle(tbPermission.getPerTitle());
                List<BaseMenu> baseMenus = permissionService.getPerMenus(tbPermission.getId());
                menuVo.setMenus(baseMenus);
                menuVos.add(menuVo);
            }
            redisService.hset(SessionKey.sessionById,sessionId,SessionCacheName.BASE_MENU,menuVos);
        }
        return menuVos;
    }
}
