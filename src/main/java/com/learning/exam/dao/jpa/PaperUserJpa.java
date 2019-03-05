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

}