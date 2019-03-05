package com.learning.exam.dao.mapper;

import com.learning.exam.model.vo.QuestionOptVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目选项持久层mapper
 * @author liuzihao
 * @date 2019-02-26  9:38
 */
@Mapper
public interface QuestionOptMapper {
    /**
     * 获取题目所有选项
     * @param qId 题目id
     * @return 选项list
     */
    List<QuestionOptVo> findQuestionOptVosByQId(@Param("qId") Integer qId);
}
