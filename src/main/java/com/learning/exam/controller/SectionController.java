package com.learning.exam.controller;

import com.learning.exam.framework.enums.PaperTypeEnum;
import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.converter.PaperTypeEnumConverter;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.model.dto.PaperSectionDto;
import com.learning.exam.model.dto.QuestionDto;
import com.learning.exam.model.entity.TbCourse;
import com.learning.exam.model.entity.TbPaper;
import com.learning.exam.model.entity.TbQuestionDb;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.model.vo.*;
import com.learning.exam.service.PaperService;
import com.learning.exam.service.QuestionService;
import com.learning.exam.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 试卷章节管理
 * @author liuzihao
 * @date 2019-02-23  10:41
 */
@Controller
@Slf4j
@RequestMapping(value = "/system/paper/section")
public class SectionController {
    @Autowired
    private PaperService paperService;
    @Autowired
    private QuestionService questionService;

    /**
     * 章节提交
     * @return html
     */
    @RequestMapping(value = "/detail/submit",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String submit(HttpServletRequest request,
                         @Validated PaperSectionDto paperSectionDto,
                         @RequestParam("paperType")Integer paperType,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            FieldError fieldError = bindingResult.getFieldError();
            throw new ValidationHtmlException(fieldError.getDefaultMessage());
        }
        PaperVo paperVo = paperService.getPaper(paperSectionDto.getPaperId());
        if(paperVo == null){
            throw new ValidationHtmlException(CodeMsg.PAPER_SELECT_ERROR);
        }
        Date startTime = DateTimeUtils.getDateInstance().dateParse(paperVo.getStartTime(),DateTimeUtils.DATE_TIME_PATTERN);
        if(startTime.getTime()<=System.currentTimeMillis()){
            throw new ValidationHtmlException(CodeMsg.PAPER_STARTTIME_OVER);
        }
        //开始前2小时
//        if(startTime.getTime()<=(System.currentTimeMillis()+3600000)){
//            throw new ValidationHtmlException(CodeMsg.PAPER_STARTTIME_LIMIT);
//        }
        if(paperType.equals(PaperTypeEnum.RANDOM_TYPE.getId())){
            List<Integer> qdbIds = Arrays.stream(paperSectionDto.getQdbIds().split(",")).map(Integer::parseInt)
                    .collect(Collectors.toList());
            for (Integer qdbId:qdbIds){
                if(questionService.getQdbIdById(qdbId)==null){
                    throw new ValidationHtmlException(CodeMsg.DB_SELECT_ERROR);
                }
            }
            if(paperSectionDto.getLevelScale().length!=QlevelEnum.values().length){
                throw new ValidationHtmlException(CodeMsg.SECTION_SCALE_ERROR);
            }
        }
        paperService.submitPaperSection(paperSectionDto);
        return ViewUtils.SUCCESS_CLOSE_PAGE;
    }

    /**
     * 章节详细
     * @return html
     */
    @RequestMapping(value = "/detail/{sectionId}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String detail(HttpServletRequest request,
                         @PathVariable("sectionId")Integer sectionId,
                         @RequestParam("t")Integer paperId,
                         @RequestParam("paperType")Integer paperType){
        if(StringUtils.isEmpty(sectionId)){
            throw new ValidationHtmlException(CodeMsg.SECTION_SELECT_ERROR);
        }
        PaperTypeEnum paperTypeEnum = PaperTypeEnumConverter.converter(paperType);
        PaperSectionVo paperSectionVo = paperService.getPaperSectionById(sectionId,paperTypeEnum);
        request.setAttribute("paperSectionVo",paperSectionVo);
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
        if(PaperTypeEnum.RANDOM_TYPE.getId().equals(paperType)){
            request.setAttribute("paperId",paperId);
            request.setAttribute("paperType",paperType);
            return "paper/section/add";
        }
        return "paper/section/form";
    }

    /**
     *增加章节
     * @return html
     */
    @RequestMapping(value = "/add/{paperId}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String add(HttpServletRequest request,
                      @PathVariable("paperId")Integer paperId,
                      @RequestParam("paperType")Integer paperType){
        List<RequestEnumVo> typeList = new ArrayList<>();
        for (QtypeEnum qtypeEnum:QtypeEnum.values()){
            RequestEnumVo requestEnumVo = new RequestEnumVo(qtypeEnum.getId(),qtypeEnum.getType());
            typeList.add(requestEnumVo);
        }
        request.setAttribute("paperId",paperId);
        request.setAttribute("paperType",paperType);
        request.setAttribute("types",typeList);
        return "paper/section/add";
    }

    /**
     * 删除章节
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/delete/{paperId}/{sectionId}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult delete(HttpServletRequest request,
                             @PathVariable("paperId")Integer paperId,
                             @PathVariable("sectionId")String sectionId){
        PaperVo paperVo = paperService.getPaper(paperId);
        if(paperVo == null){
            throw new ValidationJsonException(CodeMsg.PAPER_SELECT_ERROR);
        }
        Date startTime = DateTimeUtils.getDateInstance().dateParse(paperVo.getStartTime(),DateTimeUtils.DATE_TIME_PATTERN);
        if(startTime.getTime()<=System.currentTimeMillis()){
            throw new ValidationJsonException(CodeMsg.PAPER_STARTTIME_OVER);
        }
        //开始前2小时
//        if(startTime.getTime()<=(System.currentTimeMillis()+3600000)){
//            throw new ValidationJsonException(CodeMsg.PAPER_STARTTIME_LIMIT);
//        }
        paperService.deletePaperSection(paperId,sectionId);
        return JsonResult.success(null);
    }

    /**
     * 所有章节
     * @return html
     */
    @RequestMapping(value = "/list/{paperId}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String list(HttpServletRequest request,
                         @PathVariable("paperId")Integer paperId){
        if(StringUtils.isEmpty(paperId)){
            throw new ValidationHtmlException(CodeMsg.PAPER_SELECT_ERROR);
        }
        PaperVo paperVo = paperService.getPaper(paperId);
        if(paperVo == null){
            throw new ValidationHtmlException(CodeMsg.PAPER_SELECT_ERROR);
        }
        request.setAttribute("paperVo",paperVo);
        request.setAttribute("paperType",new RequestEnumVo(paperVo.getPaperType().getId(),paperVo.getPaperType().getType()));
        return "paper/section/list";
    }

    /**
     * 所有章节
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<PaperSectionVo> listJson(HttpServletRequest request,
                                         @RequestParam("paperId")Integer paperId,
                                         @RequestParam("paperType")Integer paperType){
        if(StringUtils.isEmpty(paperId)){
            throw new ValidationJsonException(CodeMsg.PAPER_SELECT_ERROR);
        }
        PaperTypeEnum paperTypeEnum = PaperTypeEnumConverter.converter(paperType);
        return paperService.getPaperSectionsByPaperId(paperId,paperTypeEnum);
    }

    /**
     * 章节题目
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/question/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<QuestionContentVo> questionJson(HttpServletRequest request,
                                         @RequestParam("sectionId")Integer sectionId){
        if(StringUtils.isEmpty(sectionId)){
            throw new ValidationJsonException(CodeMsg.SECTION_SELECT_ERROR);
        }
        return questionService.getQuestionContentsBySectionId(sectionId);
    }

    /**
     * 增加章节题目
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/question/add",method = {RequestMethod.POST},produces = "application/json;charset=utf-8")
    public JsonResult questionAdd(HttpServletRequest request,
                                     @RequestParam("paperId")Integer paperId,
                                     @RequestParam("sectionId")Integer sectionId,
                                     @RequestParam("questionId")String questionId){
        if(StringUtils.isEmpty(questionId)){
            throw new ValidationJsonException(CodeMsg.Q_SELECT_ERROR);
        }
        PaperVo paperVo = paperService.getPaper(paperId);
        Date startTime = DateTimeUtils.getDateInstance().dateParse(paperVo.getStartTime(),DateTimeUtils.DATE_TIME_PATTERN);
        if(startTime.getTime()<=System.currentTimeMillis()){
            throw new ValidationJsonException(CodeMsg.PAPER_STARTTIME_OVER);
        }
        //开始前2小时
//        if(startTime.getTime()<=(System.currentTimeMillis()+3600000)){
//            throw new ValidationJsonException(CodeMsg.PAPER_STARTTIME_LIMIT);
//        }
        paperService.addQuestionIdBySectionId(sectionId,questionId);
        return JsonResult.success(null);
    }

    /**
     * 删除章节题目
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/question/delete",method = {RequestMethod.POST},produces = "application/json;charset=utf-8")
    public JsonResult questionDelete(HttpServletRequest request,
                                     @RequestParam("paperId")Integer paperId,
                                     @RequestParam("sectionId")Integer sectionId,
                                     @RequestParam("questionId")Integer questionId){
        if(StringUtils.isEmpty(sectionId)){
            throw new ValidationJsonException(CodeMsg.SECTION_SELECT_ERROR);
        }
        PaperVo paperVo = paperService.getPaper(paperId);
        Date startTime = DateTimeUtils.getDateInstance().dateParse(paperVo.getStartTime(),DateTimeUtils.DATE_TIME_PATTERN);
        if(startTime.getTime()<=System.currentTimeMillis()){
            throw new ValidationJsonException(CodeMsg.PAPER_STARTTIME_OVER);
        }
        //开始前2小时
//        if(startTime.getTime()<=(System.currentTimeMillis()+3600000)){
//            throw new ValidationJsonException(CodeMsg.PAPER_STARTTIME_LIMIT);
//        }
        List<Integer> questionIds = questionService.getQuestionIdsBySectionId(sectionId);
        if(CollectionUtils.isEmpty(questionIds)){
            throw new ValidationJsonException(CodeMsg.SECTION_SELECT_ERROR);
        }
        if(questionIds.contains(questionId)){
            questionIds.remove(questionId);
        }else {
            return JsonResult.error("找不到该试题");
        }
        String questionIdsStr = questionIds.stream().map(String::valueOf)
                .collect(Collectors.joining(","));
        paperService.deleteSectionQuestionBySectionId(sectionId,questionIdsStr,questionIds.size()>=1?(questionIds.size()-1):0);
        return JsonResult.success(null);
    }

    /**
     * 配置章节所有需要题目
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/question/config/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<QuestionContentVo> configQuestionJson(HttpServletRequest request){
        String qdbKey = request.getParameter("qdbKey");
        String typeKey = request.getParameter("typeKey");
        String levelKey = request.getParameter("levelKey");
        String contentKey = request.getParameter("contentKey");
        String statusKey = request.getParameter("statusKey");
        List<QuestionContentVo> questionVos = null;
        if(StringUtils.isEmpty(qdbKey)&&StringUtils.isEmpty(typeKey)
                &&StringUtils.isEmpty(levelKey)&&StringUtils.isEmpty(contentKey)&&StringUtils.isEmpty(statusKey)){
            questionVos = questionService.getQuestionContents();
        }else {
            questionVos = questionService.getQuestionContentsByCondition(qdbKey,typeKey,levelKey,statusKey,contentKey);
        }
        return questionVos;
    }

    /**
     * 配置章节所有需要题库
     * @return html
     */
    @RequestMapping(value = "/qdb/list",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String qdbList(HttpServletRequest request){
        List<TbCourse> courses = questionService.getCourses();
        request.setAttribute("courses",courses);
        return "paper/section/qdb";
    }

    /**
     * 配置章节所有需要题库
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/qdb/list/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<QuestionDbVo> qdbListJson(HttpServletRequest request){
        List<QuestionDbVo> questionDbVos = null;
        String nameKey = request.getParameter("nameKey");
        String courseKey = request.getParameter("courseKey");
        String statusKey = request.getParameter("statusKey");
        if(StringUtils.isEmpty(nameKey)&&StringUtils.isEmpty(courseKey)&&StringUtils.isEmpty(statusKey)){
            questionDbVos = questionService.getQdbs();
        }else {
            questionDbVos = questionService.getQdbsByCondition(nameKey,"",courseKey,statusKey);
        }
        return questionDbVos;
    }
}
