package com.learning.exam.controller;

import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.model.entity.TbCourse;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 课程管理
 * @author liuzihao
 * @date 2019-02-21  11:29
 */
@Controller
@Slf4j
@RequestMapping(value = "/system/qdb/course")
public class CourseController {
    @Autowired
    private QuestionService questionService;
    @RequestMapping(value = "/detail/submit",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String submit(@RequestParam(value = "id",required = false)Integer id,
                               @RequestParam(value = "courseName")String courseName) {
        if(StringUtils.isEmpty(courseName)){
            throw new ValidationHtmlException(CodeMsg.COURSE_NAME_ERROR);
        }
        if(id==null||id==0){
            questionService.insertCourse(courseName);
        }else {
            questionService.updateCourse(courseName,id);
        }
        return ViewUtils.SUCCESS_PAGE;
    }
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String detail(HttpServletRequest request,
                               @PathVariable("id")Integer id){
        TbCourse tbCourse = questionService.getCourseById(id);
        request.setAttribute("tbCourse",tbCourse);
        return "qdb/course/form";
    }
    @ResponseBody
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult delete(@PathVariable("id")String id){
        //questionService.deleteCourse(id);
        return JsonResult.success(null);
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String courseList(){
        return "qdb/course/list";
    }
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<TbCourse> courseListJson(){
        return questionService.getCourses();
    }
}
