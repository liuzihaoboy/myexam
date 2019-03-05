package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生持久层mapper
 * @author liuzihao
 * @date 2019-02-24  12:47
 */
@Mapper
public interface StudentMapper {
    /**
     * 获取所有学生
     * @param gradeKey 条件查询：年级id
     * @param majorKey 条件查询：专业id
     * @return 学生list
     */
    List<TbStudent> findStudentsByCondition(@Param("gradeKey") String gradeKey, @Param("majorKey")String majorKey);
}
