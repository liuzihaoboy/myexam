package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学生持久层jpa
 * @author liuzihao
 * @date 2019-02-24  12:37
 */
@Repository
public interface StudentJpa extends JpaRepository<TbStudent,Integer> {
    /**
     * 获取所有学生实体
     * @param ids 用户id
     * @return 学生list
     */
    List<TbStudent> findByUserIdIn(List<Integer> ids);

    /**
     * 获取学生实体
     * @param userId 用户id
     * @return 学生
     */
    TbStudent findByUserId(Integer userId);
}
