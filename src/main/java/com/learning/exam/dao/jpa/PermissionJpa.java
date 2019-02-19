package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-01-31  12:23
 */
@Repository
public interface PermissionJpa extends JpaRepository<TbPermission,Integer> {
    @Query(nativeQuery = true,value = "select p.* from tb_permission p where p.id=:id order by id ASC")
    TbPermission findById(@Param("id")Integer id);
    List<TbPermission> findAllByIdIn(List<Integer> ids);
}
