package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-21  11:16
 */
@Repository
public interface PaperJpa extends JpaRepository<TbPaper,Integer> {
}
