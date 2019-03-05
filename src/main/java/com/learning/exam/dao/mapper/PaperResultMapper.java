package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbPaperResult;
import com.learning.exam.model.vo.PaperResultVo;
import com.learning.exam.model.vo.ScoreResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-26  10:01
 */
@Mapper
public interface PaperResultMapper {
    List<ScoreResultVo> findResultVosByPaperId(@Param("paperId")Integer paperId, @Param("nameKey") String nameKey, @Param("majorKey") String majorKey);
    List<Integer> findResultScoreByPaperId(@Param("paperId")Integer paperId);
    PaperResultVo findPaperResultByPaperUserId(@Param("paperUserId")Integer paperUserId);
    int insertPaperResult(@Param("tbPaperResult")TbPaperResult tbPaperResult);
}
