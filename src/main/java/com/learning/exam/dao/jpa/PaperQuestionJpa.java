package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaperQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author liuzihao
 * @date 2019-02-21  11:18
 */
@Repository
public interface PaperQuestionJpa extends JpaRepository<TbPaperQuestion,Integer> {
    @Query(nativeQuery = true,value = "select question_ids from tb_paper_question tpq where tpq.section_id=:sectionId")
    String findQuestionIdsBySectionId(@Param("sectionId")Integer sectionId);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_paper_question set question_ids=:questionIds where section_id=:sectionId")
    int updateQuestionsBySectionId(@Param("sectionId")Integer sectionId,@Param("questionIds")String questionIds);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_paper_question(section_id, question_ids) values (:sectionId,:questionIds)")
    int insertQuestionsBySectionId(@Param("sectionId") Integer sectionId, @Param("questionIds") String questionIds);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from tb_paper_question where section_id=:sectionId")
    int deletePaperQuestionBySectionId(@Param("sectionId")Integer sectionId);
}