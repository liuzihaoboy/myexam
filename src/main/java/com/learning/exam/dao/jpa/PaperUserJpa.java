package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaperUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-25  10:40
 */
@Repository
public interface PaperUserJpa extends JpaRepository<TbPaperUser,Integer> {
    TbPaperUser findByUserIdAndPaperId(Integer userId,Integer paperId);
    @Query(nativeQuery = true,value = "select u.id,u.paper_id,u.status,u.user_id from tb_paper_user u,tb_paper p where  u.paper_id=p.id and u.user_id=:userId and u.status in(0,3) order by start_time asc")
    List<TbPaperUser> findByUserIdSubmit(@Param("userId") Integer userId);
    @Query(nativeQuery = true,value = "select u.id,u.paper_id,u.status,u.user_id from tb_paper_user u,tb_paper p where  u.paper_id=p.id and u.user_id=:userId and u.status in(1,2) order by start_time asc")
    List<TbPaperUser> findByUserIdNoSubmit(@Param("userId") Integer userId);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from tb_paper_user where paper_id=:paperId")
    int deletePaperUserByPaperId(@Param("paperId")Integer paperId);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_paper_user set status=:status where id=:id")
    int updateStatus(@Param("status") Integer status,@Param("id")Integer id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_paper_user(user_id, paper_id, status) VALUES (:userId,:paperId,:status)")
    int insertPaperUser(@Param("userId")Integer userId,@Param("paperId")Integer paperId,@Param("status")Integer status);
}
