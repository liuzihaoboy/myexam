package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbStudentGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * 学生年级持久层jpa
 * @author liuzihao
 * @date 2019-02-24  12:38
 */
@Repository
public interface StudentGradeJpa extends JpaRepository<TbStudentGrade,Integer> {
    /**
     * 获取年级实体
     * @param gradeName 年级(2015)
     * @return 年级
     */
    TbStudentGrade findByGradeName(String gradeName);
}
