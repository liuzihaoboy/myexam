package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaperSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liuzihao
 * @date 2019-02-21  11:17
 */
@Repository
public interface PaperSectionJpa extends JpaRepository<TbPaperSection,Integer> {
}
