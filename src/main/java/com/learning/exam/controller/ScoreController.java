package com.learning.exam.controller;

import com.learning.exam.framework.exception.AuthException;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.model.entity.TbPaperUser;
import com.learning.exam.model.entity.TbStudentMajor;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.vo.*;
import com.learning.exam.service.PaperService;
import com.learning.exam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-03-03  15:55
 */
@Slf4j
@Controller
@RequestMapping(value = "/system/score")
public class ScoreController {
    @Autowired
    private PaperService paperService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String list (){
        return "score/list";
    }
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.POST,RequestMethod.GET})
    public List<ScoreVo> listJson(HttpServletRequest request){
        String nameKey = request.getParameter("nameKey");
        return paperService.getScores(nameKey);
    }
    @RequestMapping("/detail/{paperId}")
    public String detail(HttpServletRequest request,
                         @PathVariable("paperId")Integer paperId){
        PaperVo paperVo = paperService.getPaper(paperId);
        if(paperVo == null){
            throw new ValidationHtmlException(CodeMsg.PAPER_SELECT_ERROR);
        }
        paperVo.setTotalScore(paperService.getPaperTotalScore(paperId));
        List<TbStudentMajor> majors = userService.getMajors();
        request.setAttribute("majors",majors);
        request.setAttribute("paperVo",paperVo);
        request.setAttribute("paperType",new RequestEnumVo(paperVo.getPaperType().getId(),paperVo.getPaperType().getType()));
        return "score/detail";
    }
    @RequestMapping("/detail/{paperId}/{userId}")
    public String result(HttpServletRequest request,@PathVariable("paperId")Integer paperId,@PathVariable("userId")Integer userId){
        TbPaperUser tbPaperUser = paperService.getPaperUser(userId,paperId);
        if(tbPaperUser == null){
            throw new AuthException(CodeMsg.PAPER_USER_NO_JOIN);
        }
        PaperResultVo paperResultVo = paperService.getPaperResultByPaperUserId(tbPaperUser.getId());
        if(paperResultVo == null){
            throw new AuthException(CodeMsg.PAPER_NO_RESULT);
        }
        request.setAttribute("paperResultVo",paperResultVo);
        //显示答案
        List<QuestionResultVo> questions = paperService.getQuestionResultByPaperResult(paperResultVo);
        request.setAttribute("questions",questions);
        return "score/oneDetail";
    }
    @ResponseBody
    @RequestMapping(value = "/detail/{paperId}/list/json",method = {RequestMethod.POST,RequestMethod.GET})
    public List<ScoreResultVo> detailJson(@PathVariable("paperId")Integer paperId,
                                          HttpServletRequest request){
        String nameKey = request.getParameter("nameKey");
        String majorKey = request.getParameter("majorKey");
        return paperService.getScoreResultsByPaperId(paperId,nameKey,majorKey);
    }
}
