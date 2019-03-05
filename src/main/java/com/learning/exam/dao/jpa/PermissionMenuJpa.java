package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPermissionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-03  21:23
 */
@Repository
public interface PermissionMenuJpa extends JpaRepository<TbPermissionMenu,Integer> {
    /**
     * 获取指定一级权限的所有二级权限
     * @param perId 一级权限id
     * @return 二级权限list
     */
    List<TbPermissionMenu> findAllByPerId(Integer perId);
}
