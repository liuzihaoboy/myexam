package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author liuzihao
 * @date 2019-02-11  13:55
 */
@Repository
public interface QuestionJpa extends JpaRepository<TbQuestion,Integer> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_question(q_content, qdb_id, q_type, q_level, q_status, c_uid, u_uid,opt_key,key_info) values (:#{#tbQuestion.qContent},:#{#tbQuestion.qdbId},:#{#tbQuestion.qType},:#{#tbQuestion.qLevel},:#{#tbQuestion.qStatus},:#{#tbQuestion.cUid},:#{#tbQuestion.uUid},:#{#tbQuestion.optKey},:#{#tbQuestion.keyInfo})")
    int insertQuestion(@Param("tbQuestion") TbQuestion tbQuestion);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_question set q_content=:#{#tbQuestion.qContent},qdb_id=:#{#tbQuestion.qdbId},q_level=:#{#tbQuestion.qLevel},q_status=:#{#tbQuestion.qStatus},u_uid=:#{#tbQuestion.uUid},opt_key=:#{#tbQuestion.optKey},key_info=:#{#tbQuestion.keyInfo} where id=:#{#tbQuestion.id}")
    int updateQuestion(@Param("tbQuestion") TbQuestion tbQuestion);
    @Query(nativeQuery = true,value = "select tq.q_content from tb_question tq where tq.id=:id")
    String findQuestionContentById(@Param("id")Integer id);
    @Query(nativeQuery = true,value = "select q_type from tb_question where  id=:id")
    Integer findQuestionTypeById(@Param("id")Integer id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_question set opt_key=:optKey where id=:id")
    int updateOptKey(@Param("optKey") String optKey,@Param("id")Integer id);
}
