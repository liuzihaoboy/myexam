package com.learning.exam.controller;

import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.converter.QtypeEnumConverter;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.dto.QdbDto;
import com.learning.exam.model.dto.QuestionDto;
import com.learning.exam.model.entity.TbCourse;
import com.learning.exam.model.entity.TbQuestionDb;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.model.vo.QuestionDbVo;
import com.learning.exam.model.vo.QuestionVo;
import com.learning.exam.model.vo.RequestEnumVo;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.ExcelService;
import com.learning.exam.service.QuestionService;
import com.learning.exam.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 试题管理
 * @author liuzihao
 * @date 2019-02-14  16:39
 */
@Controller
@RequestMapping(value = "/system/qdb/question")
@Slf4j
public class QuestionController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ExcelService excelService;

    @RequestMapping(value = "/preview/{questionId}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String preview(HttpServletRequest request,
                          @PathVariable("questionId")Integer questionId){
        QuestionVo questionVo = questionService.getQuestionById(questionId);
        if(questionVo==null){
            throw new ValidationHtmlException(CodeMsg.Q_SELECT_ERROR);
        }
        request.setAttribute("questionVo",questionVo);
        request.setAttribute("type",new RequestEnumVo(questionVo.getQType().getId(),questionVo.getQType().getType()));
        request.setAttribute("level",new RequestEnumVo(questionVo.getQLevel().getId(),questionVo.getQLevel().getLevel()));
        return "qdb/question/preview";
    }
    @RequestMapping(value = "/detail/submit",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String submit(HttpServletRequest request,
                            @Validated QuestionDto questionDto,
                            BindingResult bindingResult) {
        HttpSession session = request.getSession();
        if(bindingResult.hasErrors()){
            FieldError fieldError = bindingResult.getFieldError();
            throw new ValidationHtmlException(fieldError.getDefaultMessage());
        }
        String[] keys = questionDto.getOptKey().split(",");
        QtypeEnum qtypeEnum = QtypeEnumConverter.converter(questionDto.getQType());
        switch (qtypeEnum){
            case EXCLUSIVE_CHOICE:{
                if(keys.length!=1){
                    throw new ValidationHtmlException(CodeMsg.Q_KEY_ERROR_ONE);
                }
                break;
            }
            case MULTIPLE_CHOICE:{
                if(keys.length<=1){
                    throw new ValidationHtmlException(CodeMsg.Q_KEY_ERROR_TWO);
                }
                break;
            }
            case TRUE_FALSE:{
                if(keys.length!=1&&!"T".equals(keys[0])&&!"F".equals(keys[0])){
                    throw new ValidationHtmlException(CodeMsg.Q_KEY_ERROR_THREE);
                }
                break;
            }
            case FILL_BLANKS:{
                int qContentBlankNum = (questionDto.getQContent().length()-questionDto.getQContent().replaceAll("（____）","").length())/6;
                if(qContentBlankNum!=keys.length){
                    throw new ValidationHtmlException(CodeMsg.Q_KEY_ERROR_FOUR);
                }
                break;
            }
            default:break;
        }
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        questionService.submitQuestion(questionDto,tbUserVo);
        return ViewUtils.SUCCESS_CLOSE_PAGE;
    }
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String detail(HttpServletRequest request,
                            @PathVariable("id")String id){
        if(StringUtils.isEmpty(id)){
            throw new ValidationHtmlException(CodeMsg.Q_SELECT_ERROR);
        }
        List<TbQuestionDb> tbQuestionDbs = questionService.getTbQdbs();
        request.setAttribute("qdbs",tbQuestionDbs);
        List<RequestEnumVo> typeList = new ArrayList<>();
        for (QtypeEnum qtypeEnum:QtypeEnum.values()){
            RequestEnumVo requestEnumVo = new RequestEnumVo(qtypeEnum.getId(),qtypeEnum.getType());
            typeList.add(requestEnumVo);
        }
        List<RequestEnumVo> levelList = new ArrayList<>();
        for (QlevelEnum qlevelEnum:QlevelEnum.values()){
            RequestEnumVo requestEnumVo = new RequestEnumVo(qlevelEnum.getId(),qlevelEnum.getLevel());
            levelList.add(requestEnumVo);
        }
        request.setAttribute("types",typeList);
        request.setAttribute("levels",levelList);
        QuestionVo questionVo = questionService.getQuestionById(Integer.parseInt(id));
        request.setAttribute("questionVo",questionVo);
        if(questionVo!=null){
            request.setAttribute("qType",new RequestEnumVo(questionVo.getQType().getId(),questionVo.getQType().getType()));
            request.setAttribute("qLevel",new RequestEnumVo(questionVo.getQLevel().getId(),questionVo.getQLevel().getLevel()));
        }
        return "qdb/question/form";
    }
    @ResponseBody
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult delete(HttpServletRequest request,
                                @PathVariable("id")String id){
        questionService.deleteQuestion(id);
        return JsonResult.success(null);
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String list(HttpServletRequest request){
        List<TbQuestionDb> tbQuestionDbs = questionService.getTbQdbs();
        request.setAttribute("qdbs",tbQuestionDbs);
        List<RequestEnumVo> typeList = new ArrayList<>();
        for (QtypeEnum qtypeEnum:QtypeEnum.values()){
            RequestEnumVo requestEnumVo = new RequestEnumVo(qtypeEnum.getId(),qtypeEnum.getType());
            typeList.add(requestEnumVo);
        }
        List<RequestEnumVo> levelList = new ArrayList<>();
        for (QlevelEnum qlevelEnum:QlevelEnum.values()){
            RequestEnumVo requestEnumVo = new RequestEnumVo(qlevelEnum.getId(),qlevelEnum.getLevel());
            levelList.add(requestEnumVo);
        }
        request.setAttribute("types",typeList);
        request.setAttribute("levels",levelList);
        return "qdb/question/list";
    }
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<QuestionVo> listJson(HttpServletRequest request){
        String qdbKey = request.getParameter("qdbKey");
        String typeKey = request.getParameter("typeKey");
        String levelKey = request.getParameter("levelKey");
        String contentKey = request.getParameter("contentKey");
        String statusKey = request.getParameter("statusKey");
        List<QuestionVo> questionVos = null;
        if(StringUtils.isEmpty(qdbKey)&&StringUtils.isEmpty(typeKey)
                &&StringUtils.isEmpty(levelKey)&&StringUtils.isEmpty(contentKey)&&StringUtils.isEmpty(statusKey)){
            questionVos = questionService.getQuestions();
        }else {
            questionVos = questionService.getQuestionsByCondition(qdbKey,typeKey,levelKey,statusKey,contentKey);
        }
        return questionVos;
    }
    @RequestMapping(value = "/upload",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String upload(HttpServletRequest request,
                         @RequestParam("file") MultipartFile file){
        String path = FileUtils.saveFile(file,"/upload");
        if(path == null){
            return ViewUtils.ERROR_PAGE;
        }
        excelService.readExcel(path,2);
        return ViewUtils.SUCCESS_PAGE;
    }
}
