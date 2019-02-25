package com.learning.exam.controller;

import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.PaperUserStatusEnum;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.vo.PaperResultVo;
import com.learning.exam.model.vo.PaperTestVo;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.PaperService;
import com.learning.exam.service.TestService;
import com.learning.exam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
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
    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(){
        return "student/welcome_student";
    }
    @ResponseBody
    @RequestMapping("/index/json")
    public List<PaperTestVo> indexJson(HttpServletRequest request){
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        return paperService.getPaperTestByUserIdNoSubmit(tbUserVo.getId());
    }
    @ResponseBody
    @RequestMapping("/index/last/json")
    public List<PaperResultVo> indexLastJson(HttpServletRequest request){
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        return paperService.getPaperResultByUserIdSubmit(tbUserVo.getId());
    }
}
