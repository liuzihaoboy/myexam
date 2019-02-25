package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaperTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author liuzihao
 * @date 2019-02-25  10:41
 */
@Repository
public interface PaperTestJpa extends JpaRepository<TbPaperTest,Integer> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_paper_test(paper_user_id, question_ids) values (:paperUserId,:questionIdsStr)")
    int insertPaperTest(@Param("paperUserId") Integer paperUserId,@Param("questionIdsStr") String questionIdsStr);
}
