package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author liuzihao
 * @date 2019-02-11  13:54
 */
@Repository
public interface CourseJpa extends JpaRepository <TbCourse,Integer> {
    TbCourse findById(Integer id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_course(course_name) values (:courseName)")
    int insertCourse(@Param("courseName") String courseName);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_course set course_name=:courseName where id=:id")
    int updateCourse(@Param("courseName") String courseName,@Param("id")Integer id);
}
