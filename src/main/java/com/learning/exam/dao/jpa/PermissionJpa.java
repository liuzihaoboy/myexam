package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限持久层jpa
 * @author liuzihao
 * @date 2019-01-31  12:23
 */
@Repository
public interface PermissionJpa extends JpaRepository<TbPermission,Integer> {
    /**
     * 获取所有指定的权限实体
     * @param ids 指定id list
     * @return 权限list
     */
    List<TbPermission> findAllByIdIn(List<Integer> ids);
}
