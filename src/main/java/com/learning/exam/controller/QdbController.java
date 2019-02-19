package com.learning.exam.controller;

import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.dto.QdbDto;
import com.learning.exam.model.entity.TbCourse;
import com.learning.exam.model.entity.TbQuestionDb;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.model.vo.QuestionDbVo;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 题库管理
 * @author liuzihao
 * @date 2019-02-03  11:04
 */
@Controller
@Slf4j
@RequestMapping(value = "/system/qdb")
public class QdbController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private QuestionService questionService;
    @RequestMapping(value = "/course/detail/submit",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String courseSubmit(HttpServletRequest request,
                            @RequestParam(value = "id",required = false)Integer id,
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
    @RequestMapping(value = "/course/detail/{id}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String courseDetail(HttpServletRequest request,
                            @PathVariable("id")Integer id){
        TbCourse tbCourse = questionService.getCourseById(id);
        request.setAttribute("tbCourse",tbCourse);
        return "qdb/course/form";
    }
    @ResponseBody
    @RequestMapping(value = "/course/delete/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult courseDelete(HttpServletRequest request,
                                @PathVariable("id")String id){
        //questionService.deleteCourse(id);
        return JsonResult.success(null);
    }
    @RequestMapping(value = "/course/list",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String courseList(HttpServletRequest request){
        return "qdb/course/list";
    }
    @ResponseBody
    @RequestMapping(value = "/course/list/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<TbCourse> courseListJson(HttpServletRequest request){
        return questionService.getCourses();
    }

    @RequestMapping(value = "/detail/submit",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String submitQdb(HttpServletRequest request,
                            @Validated QdbDto qdbDto,
                            BindingResult bindingResult) {
        HttpSession session = request.getSession();
        if(bindingResult.hasErrors()){
            FieldError fieldError = bindingResult.getFieldError();
            throw new ValidationHtmlException(fieldError.getDefaultMessage());
        }
        TbCourse tbCourse = questionService.getCourseById(qdbDto.getCourseId());
        if(tbCourse == null){
            throw new ValidationHtmlException(CodeMsg.COURSE_CHECK_ERROR);
        }
        TbQuestionDb questionDb = tbQuestionDb(qdbDto);
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        questionDb.setUUid(tbUserVo.getId());
        if(questionDb.getId()==null||questionDb.getId()==0){
            questionDb.setCUid(tbUserVo.getId());
            questionService.insertQuestionDb(questionDb);
        }else {
            questionService.updateQuestionDb(questionDb);
        }
        return ViewUtils.SUCCESS_PAGE;
    }
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String detailQdb(HttpServletRequest request,
                                @PathVariable("id")String id){
        if(StringUtils.isEmpty(id)){
            throw new ValidationHtmlException(CodeMsg.DB_SELECT_ERROR);
        }
        QuestionDbVo questionDbVo = questionService.getQdbById(Integer.parseInt(id));
        List<TbCourse> courses = questionService.getCourses();
        request.setAttribute("courses",courses);
        request.setAttribute("questionDbVo",questionDbVo);
        return "qdb/form";
    }
    @ResponseBody
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult deleteQdb(HttpServletRequest request,
                                @PathVariable("id")String id){
        //questionService.deleteQuestionDb(id);
        return JsonResult.success(null);
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String list(HttpServletRequest request){
        List<TbCourse> courses = questionService.getCourses();
        request.setAttribute("courses",courses);
        return "qdb/list";
    }
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<QuestionDbVo> listJson(HttpServletRequest request){
        List<QuestionDbVo> questionDbVos = null;
        String nameKey = request.getParameter("nameKey");
        String userNameKey = request.getParameter("userNameKey");
        String courseKey = request.getParameter("courseKey");
        String statusKey = request.getParameter("statusKey");
        if(StringUtils.isEmpty(nameKey)&&StringUtils.isEmpty(userNameKey)
                &&StringUtils.isEmpty(courseKey)&&StringUtils.isEmpty(statusKey)){
            questionDbVos = questionService.getQdbs();
        }else {
            questionDbVos = questionService.getQdbsByCondition(nameKey,userNameKey,courseKey,statusKey);
        }
        return questionDbVos;
    }
    private TbQuestionDb tbQuestionDb(QdbDto qdbDto){
        TbQuestionDb questionDb = new TbQuestionDb();
        Integer id = qdbDto.getId();
        if(id!=null){
            questionDb.setId(id);
        }
        questionDb.setQdbName(qdbDto.getQdbName());
        questionDb.setQdbInfo(qdbDto.getQdbInfo());
        questionDb.setQdbStatus(qdbDto.getQdbStatus());
        questionDb.setCourseId(qdbDto.getCourseId());
        return questionDb;
    }
}
