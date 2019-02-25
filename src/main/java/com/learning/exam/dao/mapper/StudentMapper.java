package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-24  12:47
 */
@Mapper
public interface StudentMapper {
    List<TbStudent> findStudentsByCondition(@Param("gradeKey") String gradeKey, @Param("majorKey")String majorKey);
}
