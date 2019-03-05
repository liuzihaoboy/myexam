package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbQuestionDb;
import com.learning.exam.model.vo.QuestionContentVo;
import com.learning.exam.model.vo.QuestionDbVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题库持久层mapper
 * @author liuzihao
 * @date 2019-02-12  10:27
 */
@Mapper
public interface QuestionDbMapper {
    /**
     * 获取所有题库
     * @param nameKey 条件查询：题库名
     * @param userNameKey 条件查询：创建人名
     * @param courseKey 条件查询：课程id
     * @param statusKey 条件查询：题库状态
     * @return 题库list
     */
    List<TbQuestionDb> findQdbsByCondition(@Param("nameKey")String nameKey, @Param("userNameKey")String userNameKey, @Param("courseKey")String courseKey,@Param("statusKey")String statusKey);
}
