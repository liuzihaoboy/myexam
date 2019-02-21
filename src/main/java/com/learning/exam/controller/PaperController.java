package com.learning.exam.controller;

import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.entity.TbCourse;
import com.learning.exam.model.vo.QuestionDbVo;
import com.learning.exam.service.PaperService;
import com.learning.exam.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-21  11:27
 */
@Controller
@Slf4j
@RequestMapping(value = "/system/paper")
public class PaperController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String list(){
        return "paper/list";
    }
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public void listJson(HttpServletRequest request){

    }
}