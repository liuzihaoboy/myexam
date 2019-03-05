package com.learning.exam.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.PaperUserStatusEnum;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.framework.redis.keys.PaperKey;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.keys.UserKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.entity.TbPaperTest;
import com.learning.exam.model.entity.TbPaperUser;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.vo.*;
import com.learning.exam.service.PaperService;
import com.learning.exam.service.TestService;
import com.learning.exam.service.UserService;
import com.learning.exam.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    @RequestMapping(value = "/paper/ping",method = {RequestMethod.GET})
    public JsonResult ping(){
        return JsonResult.success(null);
    }

    @RequestMapping(value = "/expose/{paperId}",method = {RequestMethod.GET})
    public JsonResult expose(HttpServletRequest request,
                             @PathVariable("paperId")Integer paperId){
        if(paperId == null|| paperId==0){
            throw new ValidationJsonException(CodeMsg.PAPER_SELECT_ERROR);
        }
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        String url = testService.exposeUrl(Integer.toString(tbUserVo.getId()),Integer.toString(paperId));
        return JsonResult.success(url);
    }
    @RequestMapping(value = "/test/start/{paperId}/{t}.json",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult start(HttpServletRequest request,
                            @PathVariable("paperId")Integer paperId,
                            @PathVariable("t")String verifyId){
        //获取缓存里的试卷
        //返回
        if(paperId == null|| paperId==0){
            throw new ValidationJsonException(CodeMsg.PAPER_SELECT_ERROR);
        }
        if(StringUtils.isEmpty(verifyId)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,request.getSession().getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        String userId = Integer.toString(tbUserVo.getId());
        String cacheVerifyId = redisService.hget(UserKey.userById,userId,SessionCacheName.VERIFY_ID+":"+paperId,String.class);
        if(!verifyId.equals(cacheVerifyId)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        StudentPaperVo res = testService.getPaperTestQuestion(userId,Integer.toString(paperId));
        return JsonResult.success(res);
    }
    @RequestMapping(value = "/test/save/{paperId}/{t}.json",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult save(HttpServletRequest request,
                             @PathVariable("paperId")Integer paperId,
                             @PathVariable("t")String verifyId,
                           @RequestParam(value = "saveKey",required = false)String saveKey){
        if(paperId == null|| paperId==0){
            throw new ValidationJsonException(CodeMsg.PAPER_SELECT_ERROR);
        }
        if(StringUtils.isEmpty(verifyId)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,request.getSession().getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        String userId = Integer.toString(tbUserVo.getId());
        String cacheVerifyId = redisService.hget(UserKey.userById,userId,SessionCacheName.VERIFY_ID+":"+paperId,String.class);
        if(!verifyId.equals(cacheVerifyId)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        testService.savePaperTest(userId,Integer.toString(paperId), saveKey);
        return JsonResult.success(null);
    }
    @RequestMapping(value = "/test/submit/{paperId}/{t}.json",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult submit(HttpServletRequest request,
                             @PathVariable("paperId")Integer paperId,
                             @PathVariable("t")String verifyId,
                             @RequestParam("optKey")String optKey){
        if(paperId == null || paperId==0){
            throw new ValidationJsonException(CodeMsg.PAPER_SELECT_ERROR);
        }
        if(StringUtils.isEmpty(verifyId)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,request.getSession().getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        String userId = Integer.toString(tbUserVo.getId());
        String cacheVerifyId = redisService.hget(UserKey.userById,userId,SessionCacheName.VERIFY_ID+":"+paperId,String.class);
        if(!verifyId.equals(cacheVerifyId)){
            throw new ValidationJsonException(CodeMsg.PAPER_URL_ERROR);
        }
        testService.submitPaperTest(userId,Integer.toString(paperId),optKey);
        return JsonResult.success(null);
    }

}
