package com.learning.exam.service;

import com.learning.exam.framework.enums.PaperTypeEnum;
import com.learning.exam.framework.enums.PaperUserStatusEnum;
import com.learning.exam.model.dto.PaperDto;
import com.learning.exam.model.dto.PaperSectionDto;
import com.learning.exam.model.entity.TbPaperUser;
import com.learning.exam.model.vo.*;

import java.util.Date;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-21  13:24
 */
public interface PaperService {
    List<QuestionResultVo> getQuestionResultByPaperResult(PaperResultVo paperResultVo);
    PaperResultVo getPaperResultByPaperUserId(Integer paperUserId);
    TbPaperUser getPaperUser(Integer userId, Integer paperId);
    List<PaperResultVo> getPaperResultByUserIdSubmit(Integer userId);
    List<PaperTestVo> getPaperTestByUserIdNoSubmit(Integer userId);
    PaperTestVo getPaperTestByUserIdAndPaperId(Integer userId,Integer paperId);
    List<Integer> getPaperSectionIdByPaperId(Integer paperId);
    void deletePaperSection(Integer paperId,String sectionId);
    void submitPaperSection(PaperSectionDto paperSectionDto);
    void addQuestionIdBySectionId(Integer sectionId,String questionIdsStr);
    void deleteSectionQuestionBySectionId(Integer sectionId,String questionIdsStr,Integer questionNum);
    PaperSectionVo getPaperSectionById(Integer sectionId,PaperTypeEnum paperTypeEnum);
    List<PaperSectionVo> getPaperSectionsByPaperId(Integer paperId,PaperTypeEnum paperTypeEnum);
    void submitPaper(PaperDto paperDto, Integer cUserId, PaperTypeEnum paperTypeEnum);
    void deletePaper(String id);
    Date getPaperStartTimeById(Integer paperId);
    PaperVo getPaper(Integer paperId);
    Integer getPaperIdById(Integer paperId);
    Integer getPaperTotalScore(Integer paperId);
    List<ScoreResultVo> getScoreResultsByPaperId(Integer paperId, String nameKey, String majorKey);
    List<ScoreVo> getScores(String nameKey);
    List<PaperVo> getPapers();
    List<PaperVo> getPapersByCondition(String nameKey, String userNameKey, String courseKey, String typeKey, String configkey);
}
