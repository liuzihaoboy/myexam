package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbStudentGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author liuzihao
 * @date 2019-02-24  12:38
 */
@Repository
public interface StudentGradeJpa extends JpaRepository<TbStudentGrade,Integer> {
    TbStudentGrade findByGradeName(String gradeName);
}
