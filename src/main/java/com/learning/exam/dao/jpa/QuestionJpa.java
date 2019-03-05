package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * 题目持久层jpa
 * @author liuzihao
 * @date 2019-02-11  13:55
 */
@Repository
public interface QuestionJpa extends JpaRepository<TbQuestion,Integer> {
    /**
     * 获取题目类型
     * @param id 题目id
     * @return 类型
     */
    @Query(nativeQuery = true,value = "select q_type from tb_question where  id=:id")
    Integer findQuestionTypeById(@Param("id")Integer id);

    /**
     * 修改题目答案
     * @param optKey 答案
     * @param id 题目id
     * @return 修改行数
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_question set opt_key=:optKey where id=:id")
    int updateOptKey(@Param("optKey") String optKey,@Param("id")Integer id);
}
