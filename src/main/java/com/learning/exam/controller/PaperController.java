package com.learning.exam.controller;

import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.PaperTypeEnum;
import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.converter.PaperTypeEnumConverter;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.dto.PaperDto;
import com.learning.exam.model.dto.QdbDto;
import com.learning.exam.model.entity.TbCourse;
import com.learning.exam.model.entity.TbQuestionDb;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.model.vo.*;
import com.learning.exam.service.PaperService;
import com.learning.exam.service.QuestionService;
import com.learning.exam.service.UserService;
import com.learning.exam.util.DateTimeUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 考试管理
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
    @Autowired
    private UserService userService;

    /**
     * 试卷提交
     * @return html
     */
    @RequestMapping(value = "/detail/submit",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String submit(HttpServletRequest request,
                         @Validated PaperDto paperDto,
                         BindingResult bindingResult) {
        HttpSession session = request.getSession();
        if(bindingResult.hasErrors()){
            FieldError fieldError = bindingResult.getFieldError();
            throw new ValidationHtmlException(fieldError.getDefaultMessage());
        }
        if(paperDto.getId()!=null){
            Date startTime = paperService.getPaperStartTimeById(paperDto.getId());
            if(startTime.getTime()<=System.currentTimeMillis()){
                throw new ValidationHtmlException(CodeMsg.PAPER_STARTTIME_OVER);
            }
            //开始前2小时
            if(startTime.getTime()<=(System.currentTimeMillis()+3600000)){
                throw new ValidationHtmlException(CodeMsg.PAPER_STARTTIME_LIMIT);
            }
        }
        Integer showKey = paperDto.getShowKey();
        if(showKey!=1&&showKey!=0){
            throw new ValidationHtmlException("公布答案类型错误");
        }
        Integer configStatus = paperDto.getConfigStatus();
        if(configStatus!=0&&configStatus!=1){
            throw new ValidationHtmlException("试题配置类型错误");
        }
        if((paperDto.getStartTime().getTime()+paperDto.getPaperMinute()*60000)>paperDto.getEndTime().getTime()){
            throw new ValidationHtmlException("时间设定错误");
        }
        TbCourse tbCourse = questionService.getCourseById(paperDto.getCourseId());
        if(tbCourse == null){
            throw new ValidationHtmlException(CodeMsg.COURSE_CHECK_ERROR);
        }
        PaperTypeEnum paperTypeEnum = PaperTypeEnumConverter.converter(paperDto.getPaperType());
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        paperService.submitPaper(paperDto,tbUserVo.getId(),paperTypeEnum);
        return ViewUtils.SUCCESS_CLOSE_PAGE;
    }

    /**
     * 试卷详细
     * @return html
     */
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String detail(HttpServletRequest request,
                         @PathVariable("id")String id){
        if(StringUtils.isEmpty(id)){
            throw new ValidationHtmlException(CodeMsg.PAPER_SELECT_ERROR);
        }
        List<TbCourse> courses = questionService.getCourses();
        request.setAttribute("courses",courses);
        List<RequestEnumVo> typeList = new ArrayList<>();
        for (PaperTypeEnum paperTypeEnum:PaperTypeEnum.values()){
            RequestEnumVo requestEnumVo = new RequestEnumVo(paperTypeEnum.getId(),paperTypeEnum.getType());
            typeList.add(requestEnumVo);
        }
        request.setAttribute("types",typeList);
        PaperVo paperVo = paperService.getPaper(Integer.parseInt(id));
        request.setAttribute("paperVo",paperVo);
        if(paperVo != null){
            request.setAttribute("paperType",paperVo.getPaperType().getId());
        }
        return "paper/form";
    }

    /**
     * 试卷添加用户
     * @return html
     */
    @RequestMapping(value = "/detail/user/{type}",method = RequestMethod.GET,produces="text/html;charset=utf-8")
    public String user(HttpServletRequest request,
                       @PathVariable("type")Integer type,
                       @RequestParam(required = false,value = "t")String ids){
        if(type!=0&&type!=1){
            throw new ValidationHtmlException("类型错误");
        }
        request.setAttribute("type",type);
        if(!StringUtils.isEmpty(ids)&&type==0)request.setAttribute("t",ids);
        request.setAttribute("majors",userService.getStudentMajors());
        request.setAttribute("grades",userService.getStudentGrades());
        return "paper/user";
    }

    /**
     * 试卷添加用户
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/detail/user/json",method = {RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<StudentVo> userJson(HttpServletRequest request,
                                    @RequestParam(value = "type")Integer type,
                                    @RequestParam(required = false,value = "t")String ids){
        if(type==0){
            if(!StringUtils.isEmpty(ids)){
                return userService.getStudentByIds(ids);
            }
           else {
                return null;
            }
        }
        String gradeKey = request.getParameter("gradeKey");
        String majorKey = request.getParameter("majorKey");
        List<StudentVo> studentVos = null;
        if(StringUtils.isEmpty(gradeKey)&&StringUtils.isEmpty(majorKey)){
            studentVos = userService.getStudents();
        }else {
            studentVos = userService.getStudentsByCondition(gradeKey,majorKey);
        }
        return studentVos;
    }

    /**
     * 删除试卷
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult delete(HttpServletRequest request,
                             @PathVariable("id")String id){
        paperService.deletePaper(id);
        return JsonResult.success(null);
    }

    /**
     * 所有试卷
     * @return html
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String list(HttpServletRequest request){
        List<TbCourse> courses = questionService.getCourses();
        request.setAttribute("courses",courses);
        List<RequestEnumVo> typeList = new ArrayList<>();
        for (PaperTypeEnum paperTypeEnum:PaperTypeEnum.values()){
            RequestEnumVo requestEnumVo = new RequestEnumVo(paperTypeEnum.getId(),paperTypeEnum.getType());
            typeList.add(requestEnumVo);
        }
        request.setAttribute("types",typeList);
        return "paper/list";
    }

    /**
     * 所有试卷
     * @return html
     */
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<PaperVo> listJson(HttpServletRequest request){
        List<PaperVo> paperVos = null;
        String nameKey = request.getParameter("nameKey");
        String userNameKey = request.getParameter("userNameKey");
        String courseKey = request.getParameter("courseKey");
        String typeKey = request.getParameter("typeKey");
        String configKey = request.getParameter("configKey");
        if(StringUtils.isEmpty(nameKey)&&StringUtils.isEmpty(userNameKey)
                &&StringUtils.isEmpty(courseKey)&&StringUtils.isEmpty(typeKey)
                &&StringUtils.isEmpty(configKey)){
            paperVos = paperService.getPapers();
        }else {
            paperVos = paperService.getPapersByCondition(nameKey,userNameKey,courseKey,typeKey,configKey);
        }
        return paperVos;
    }
}