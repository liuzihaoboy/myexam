package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaperTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * 用户考试信息持久层jpa
 * @author liuzihao
 * @date 2019-02-25  10:41
 */
@Repository
public interface PaperTestJpa extends JpaRepository<TbPaperTest,Integer> {
}
