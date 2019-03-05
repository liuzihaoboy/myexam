package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * 课程持久层jpa
 * @author liuzihao
 * @date 2019-02-11  13:54
 */
@Repository
public interface CourseJpa extends JpaRepository <TbCourse,Integer> {
    /**
     * 返回课程实体
     * @param id 课程id
     * @return 课程实体
     */
    TbCourse findById(Integer id);

    /**
     * 添加课程
     * @param courseName 课程名
     * @return 修改行数
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_course(course_name) values (:courseName)")
    int insertCourse(@Param("courseName") String courseName);

    /**
     * 修改课程
     * @param courseName 课程名
     * @param id 课程id
     * @return 修改行数
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_course set course_name=:courseName where id=:id")
    int updateCourse(@Param("courseName") String courseName,@Param("id")Integer id);
}
