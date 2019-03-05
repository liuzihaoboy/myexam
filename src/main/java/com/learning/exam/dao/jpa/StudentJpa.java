package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-24  12:37
 */
@Repository
public interface StudentJpa extends JpaRepository<TbStudent,Integer> {
    List<TbStudent> findByUserIdIn(List<Integer> ids);
    TbStudent findByUserId(Integer userId);
}
