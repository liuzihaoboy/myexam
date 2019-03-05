package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbQuestionDb;
import com.learning.exam.model.vo.QuestionDbVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * 题库持久层jpa
 * @author liuzihao
 * @date 2019-02-11  13:56
 */
@Repository
public interface QuestionDbJpa extends JpaRepository<TbQuestionDb,Integer> {
    /**
     * 插入题库
     * @param tbQuestionDb 题库实体
     * @return 修改行数
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_question_db(qdb_name, qdb_info, course_id, qdb_status, c_uid, u_uid) values (:#{#tbQuestionDb.qdbName},:#{#tbQuestionDb.qdbInfo},:#{#tbQuestionDb.courseId},:#{#tbQuestionDb.qdbStatus},:#{#tbQuestionDb.cUid},:#{#tbQuestionDb.uUid})")
    int insertQuestionDb(@Param("tbQuestionDb") TbQuestionDb tbQuestionDb);

    /**
     * 修改题库
     * @param tbQuestionDb 题库实体
     * @return 修改行数
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_question_db set qdb_name=:#{#tbQuestionDb.qdbName},qdb_info=:#{#tbQuestionDb.qdbInfo},qdb_status=:#{#tbQuestionDb.qdbStatus},course_id=:#{#tbQuestionDb.courseId},u_uid=:#{#tbQuestionDb.uUid}  where id=:#{#tbQuestionDb.id}")
    int updateQuestionDb(@Param("tbQuestionDb") TbQuestionDb tbQuestionDb);

    /**
     * 获取题库名称
     * @param id 题库id
     * @return 名称
     */
    @Query(nativeQuery = true,value = "select tqd.qdb_name from tb_question_db tqd where tqd.id=:id")
    String findQdbNameById(@Param("id")Integer id);

    /**
     * 获取题库id
     * @param id 题库id
     * @return 返回id
     */
    @Query(nativeQuery = true,value = "select id from tb_question_db tqd where tqd.id=:id")
    Integer findQdbIdById(@Param("id")Integer id);
}
