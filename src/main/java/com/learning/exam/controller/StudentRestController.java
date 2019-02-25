package com.learning.exam.controller;

import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.PaperUserStatusEnum;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.keys.UserKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.entity.TbPaperUser;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.vo.PaperTestVo;
import com.learning.exam.model.vo.PaperUserVo;
import com.learning.exam.model.vo.QuestionTestVo;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.PaperService;
import com.learning.exam.service.TestService;
import com.learning.exam.service.UserService;
import com.learning.exam.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-24  20:15
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class StudentRestController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private TestService testService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/expose/{paperId}",method = {RequestMethod.GET})
    public JsonResult expose(HttpServletRequest request,
                             @PathVariable("paperId")Integer paperId){
        if(paperId == null || paperId==0){
            throw new ValidationJsonException(CodeMsg.PAPER_SELECT_ERROR);
        }
        String url = testService.exposeUrl(request.getSession().getId(),paperId);
        return JsonResult.success(url);
    }
    @RequestMapping(value = "/test/{paperId}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult test(HttpServletRequest request,
                           @PathVariable("paperId")Integer paperId,
                           @RequestParam("t")String uuid){
        //修改tbPaperUser的status为2正在考试
        //创建tbPaperTest存入数据库，写入redis
        //返回获取试卷链接
        if(paperId == null || paperId==0){
            throw new ValidationJsonException(CodeMsg.PAPER_SELECT_ERROR);
        }
        if(StringUtils.isEmpty(uuid)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,request.getSession().getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        String cacheUuid = redisService.hget(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),SessionCacheName.UUID,String.class);
        if(!uuid.equals(cacheUuid)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        String verifyId = redisService.hget(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),SessionCacheName.VERIFY_ID,String.class);
        if(StringUtils.isEmpty(verifyId)){
            verifyId = SnowFlake.getInstance().nextId();
            redisService.hset(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),SessionCacheName.VERIFY_ID,verifyId);
        }
        //异步方法
        testService.writeTestInfo(tbUserVo.getId(),paperId);
        return JsonResult.success("../test/start/"+paperId+"?t="+verifyId);
    }
    @RequestMapping(value = "/test/start/{paperId}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult start(HttpServletRequest request,
                            @PathVariable("paperId")Integer paperId,
                            @RequestParam("t")String verifyId){
        //获取缓存里的试卷
        //返回
        if(paperId == null || paperId==0){
            throw new ValidationJsonException(CodeMsg.PAPER_SELECT_ERROR);
        }
        if(StringUtils.isEmpty(verifyId)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,request.getSession().getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        String cacheVerifyId = redisService.hget(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),SessionCacheName.VERIFY_ID,String.class);
        if(!verifyId.equals(cacheVerifyId)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        List<QuestionTestVo> questions = testService.getPaperTestQuestion(tbUserVo.getId(),paperId);
        return JsonResult.success(questions);
    }
}
