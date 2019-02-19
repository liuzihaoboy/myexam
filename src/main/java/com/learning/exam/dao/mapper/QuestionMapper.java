package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-16  14:56
 */
@Mapper
public interface QuestionMapper {
    List<TbQuestion> findQuestionsByCondition(@Param("qdbKey") String qdbKey, @Param("typeKey") String typeKey, @Param("levelKey") String levelKey,@Param("statusKey")String statusKey, @Param("contentKey") String contentKey);
}
