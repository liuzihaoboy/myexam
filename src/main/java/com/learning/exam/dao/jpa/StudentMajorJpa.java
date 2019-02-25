package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbStudentMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liuzihao
 * @date 2019-02-24  12:38
 */
@Repository
public interface StudentMajorJpa extends JpaRepository<TbStudentMajor,Integer> {
}
