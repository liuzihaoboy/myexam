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
}
