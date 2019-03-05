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
 * 题目选项持久层jpa
 * @author liuzihao
 * @date 2019-02-11  13:57
 */
@Repository
public interface QuestionOptJpa extends JpaRepository<TbQuestionOpt,Integer> {
    /**
     * 删除题目选项
     * @param id 选项id
     * @return 修改行数
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from tb_question_opt where id=:id")
    int deleteById(@Param("id") Integer id);

    /**
     * 获取题目所有选项id
     * @param qId 题目id
     * @return 选项id list
     */
    @Query(nativeQuery = true,value = "select id from tb_question_opt where q_id=:qId")
    Integer[] findIdsByQId(@Param("qId")Integer qId);
}
