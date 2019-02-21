package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaperQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liuzihao
 * @date 2019-02-21  11:18
 */
@Repository
public interface PaperQuestionJpa extends JpaRepository<TbPaperQuestion,Integer> {
}
