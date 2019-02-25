package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaperResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author liuzihao
 * @date 2019-02-25  10:41
 */
@Repository
public interface PaperResultJpa extends JpaRepository<TbPaperResult,Integer> {
    @Query(nativeQuery = true,value = "select r.* from tb_paper_user u,tb_paper_test t,tb_paper_result r where r.paper_test_id=t.id and t.paper_user_id=u.id and u.id=:paperUserId")
    TbPaperResult findByPaperUserId(@Param("paperUserId")Integer paperUserId);
}
