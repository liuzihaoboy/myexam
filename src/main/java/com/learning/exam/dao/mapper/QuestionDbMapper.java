package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbQuestionDb;
import com.learning.exam.model.vo.QuestionDbVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-12  10:27
 */
@Mapper
public interface QuestionDbMapper {
    List<TbQuestionDb> findQdbsByCondition(@Param("nameKey")String nameKey, @Param("userNameKey")String userNameKey, @Param("courseKey")Integer courseKey,@Param("statusKey")String statusKey);
}
