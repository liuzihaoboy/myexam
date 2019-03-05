package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbPaperResult;
import com.learning.exam.model.vo.PaperResultVo;
import com.learning.exam.model.vo.ScoreResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试结果持久层mapper
 * @author liuzihao
 * @date 2019-02-26  10:01
 */
@Mapper
public interface PaperResultMapper {
    /**
     * 获取学生考试结果list
     * @param paperId 试卷id
     * @param nameKey 条件查询：试卷名
     * @param majorKey 条件查询：学生专业
     * @return 学生考试结果
     */
    List<ScoreResultVo> findResultVosByPaperId(@Param("paperId")Integer paperId, @Param("nameKey") String nameKey, @Param("majorKey") String majorKey);

    /**
     * 获取所有用户考试分数
     * @param paperId 试卷id
     * @return 分数list
     */
    List<Integer> findResultScoreByPaperId(@Param("paperId")Integer paperId);

    /**
     * 获取用户考试结果
     * @param paperUserId 用户考试关系id
     * @return 考试结果
     */
    PaperResultVo findPaperResultByPaperUserId(@Param("paperUserId")Integer paperUserId);

    /**
     * 插入用户考试结果实体
     * @param tbPaperResult 考试结果实体
     * @return 修改行数
     */
    int insertPaperResult(@Param("tbPaperResult")TbPaperResult tbPaperResult);
}
