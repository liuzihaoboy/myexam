package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liuzihao
 * @date 2019-02-21  11:16
 */
@Repository
public interface PaperJpa extends JpaRepository<TbPaper,Integer> {
}
