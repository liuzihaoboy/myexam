package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色持久层jpa
 * @author liuzihao
 * @date 2019-01-31  12:21
 */
@Repository
public interface RoleJpa extends JpaRepository<TbRole,Integer> {
    /**
     * 获取角色实体
     * @param id 角色id
     * @return 角色
     */
    @Query(nativeQuery = true,value = "select r.* from tb_role r where r.id=:id")
    TbRole findById(@Param("id")Integer id);
}
