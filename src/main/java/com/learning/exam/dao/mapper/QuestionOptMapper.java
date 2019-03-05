package com.learning.exam.dao.mapper;

import com.learning.exam.model.vo.QuestionOptVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-26  9:38
 */
@Mapper
public interface QuestionOptMapper {
    List<QuestionOptVo> findQuestionOptVosByQId(@Param("qId") Integer qId);
}
