package com.learning.exam.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.PaperUserStatusEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.converter.QtypeEnumConverter;
import com.learning.exam.framework.exception.AuthException;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.framework.redis.keys.PaperKey;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.keys.UserKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.entity.TbPaperTest;
import com.learning.exam.model.entity.TbPaperUser;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.model.vo.*;
import com.learning.exam.service.PaperService;
import com.learning.exam.service.QuestionService;
import com.learning.exam.service.TestService;
import com.learning.exam.service.UserService;
import com.learning.exam.util.DateTimeUtils;
import com.learning.exam.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生端模块
 * @author liuzihao
 * @date 2019-02-25  17:53
 */
@Controller
@Slf4j
@RequestMapping("/user")
public class StudentController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private TestService testService;

    /**
     * 正在进行考试
     * @return html
     */
    @RequestMapping("/paper/list")
    public String list(){
        return "student/list";
    }

    /**
     * 已经参加考试
     * @return html
     */
    @RequestMapping("/paper/history")
    public String history(){
        return "student/history";
    }

    /**
     * 考试倒计时页面
     * @return html
     */
    @RequestMapping("/paper/{paperId}/{t}.html")
    public String paper(HttpServletRequest request,
                        @PathVariable("paperId")Integer paperId,
                        @PathVariable("t")String uuid){
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        PaperTestVo paperTestVo = paperService.getPaperTestByUserIdAndPaperId(tbUserVo.getId(),paperId);
        paperTestVo.setSections(null);
        paperTestVo.setQuestionIds(null);
        request.setAttribute("paperTestVo",paperTestVo);
        //减1秒
        paperTestVo.setStartTime(new Date(paperTestVo.getStartTime().getTime()-1000));
        if(paperTestVo.getEndTime().getTime()<=System.currentTimeMillis()){
            request.setAttribute("end",true);
        }
        if(paperTestVo.getStartTime().getTime()<=System.currentTimeMillis()){
            request.setAttribute("start",true);
        }
        return "student/paper";
    }

    /**
     * 考试结果详细
     * @return html
     */
    @RequestMapping("/detail/{paperId}.html")
    public String detail(HttpServletRequest request,
                        @PathVariable("paperId")Integer paperId){
        if(paperId == null || paperId==0){
            throw new AuthException(CodeMsg.PAPER_SELECT_ERROR);
        }
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        TbPaperUser tbPaperUser = paperService.getPaperUser(tbUserVo.getId(),paperId);
        if(tbPaperUser == null){
            throw new AuthException(CodeMsg.PAPER_USER_NO_JOIN);
        }
        PaperResultVo paperResultVo = paperService.getPaperResultByPaperUserId(tbPaperUser.getId());
        if(paperResultVo == null){
            throw new AuthException(CodeMsg.PAPER_NO_RESULT);
        }
        request.setAttribute("paperResultVo",paperResultVo);
        List<QuestionResultVo> questions = paperService.getQuestionResultByPaperResult(paperResultVo);
        request.setAttribute("questions",questions);
        return "student/detail";
    }

    /**
     * 正在进行考试
     * @return json
     */
    @ResponseBody
    @RequestMapping("/index/json")
    public List<PaperTestVo> indexJson(HttpServletRequest request){
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        return paperService.getPaperTestByUserIdNoSubmit(tbUserVo.getId());
    }

    /**
     * 已经参加考试
     * @return json
     */
    @ResponseBody
    @RequestMapping("/index/last/json")
    public List<PaperResultVo> indexLastJson(HttpServletRequest request){
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        return paperService.getPaperResultByUserIdSubmit(tbUserVo.getId());
    }

    /**
     * 考试页面
     * @return html
     */
    @RequestMapping(value = "/test/{paperId}/{t}.html",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String test(HttpServletRequest request,
                       @PathVariable("paperId")Integer paperId,
                       @PathVariable("t")String uuid){
        //修改tbPaperUser的status为2正在考试
        //创建tbPaperTest存入数据库，写入redis
        //返回获取试卷链接
        if(paperId == null || paperId==0){
            throw new AuthException(CodeMsg.PAPER_SELECT_ERROR);
        }
        if(StringUtils.isEmpty(uuid)){
            throw new AuthException(CodeMsg.PAPER_URL_ERROR);
        }
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,request.getSession().getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        String userId = Integer.toString(tbUserVo.getId());
        String cacheUuid = redisService.hget(UserKey.userById,userId,SessionCacheName.UUID+":"+paperId,String.class);
        if(!uuid.equals(cacheUuid)){
            throw new AuthException(CodeMsg.PAPER_URL_ERROR);
        }
        String verifyId = redisService.hget(UserKey.userById,userId,SessionCacheName.VERIFY_ID,String.class);
        if(StringUtils.isEmpty(verifyId)){
            verifyId = SnowFlake.getInstance().nextId();
            redisService.hset(UserKey.userById,userId,SessionCacheName.VERIFY_ID+":"+paperId,verifyId);
        }
        //异步方法
        testService.writeTestInfo(userId,Integer.toString(paperId));
        request.setAttribute("student",tbUserVo.getStudentVo());
        request.setAttribute("paperId",paperId);
        request.setAttribute("tid",verifyId);
        return "student/test";
    }

    /**
     * ping
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/paper/ping",method = {RequestMethod.GET})
    public JsonResult ping(){
        return JsonResult.success(null);
    }

    /**
     * 暴露考试链接
     * @return json
     */
    @ResponseBody
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

    /**
     * 考试开始
     * @return json
     */
    @ResponseBody
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

    /**
     * 保存用户考试答案
     * @return json
     */
    @ResponseBody
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

    /**
     * 提交考试
     * @return json
     */
    @ResponseBody
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
