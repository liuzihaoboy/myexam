package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbQuestionOpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-11  13:57
 */
@Repository
public interface QuestionOptJpa extends JpaRepository<TbQuestionOpt,Integer> {
//    @Query(nativeQuery = true,value = "select opt.id,opt.option_content,opt.order_num,opt.q_id from tb_question_opt opt where opt.q_id=:qId order by opt.order_num asc")
//    List<TbQuestionOpt> findByQId(@Param("qId") Integer qId);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_question_opt(option_content, q_id, order_num) values (:#{#tbQuestionOpt.optionContent},:#{#tbQuestionOpt.qId},:#{#tbQuestionOpt.orderNum})")
    int insertQuestionOpt(@Param("tbQuestionOpt") TbQuestionOpt tbQuestionOpt);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_question_opt set option_content=:#{#tbQuestionOpt.optionContent},q_id=:#{#tbQuestionOpt.qId},order_num=:#{#tbQuestionOpt.orderNum} where id=:#{#tbQuestionOpt.id}")
    int updateQuestionOpt(@Param("tbQuestionOpt") TbQuestionOpt tbQuestionOpt);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from tb_question_opt where id=:id")
    int deleteById(@Param("id") Integer id);
    @Query(nativeQuery = true,value = "select id from tb_question_opt where q_id=:qId")
    Integer[] findIdsByQId(@Param("qId")Integer qId);
}
