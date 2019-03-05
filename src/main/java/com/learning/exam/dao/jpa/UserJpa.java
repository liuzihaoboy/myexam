package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-01-31  10:29
 */
@Repository
public interface UserJpa extends JpaRepository<TbUser,Integer> {
    @Query(nativeQuery = true,value = "select u.* from tb_user u where u.account=:name and u.password=:pwd and u.role_id=:role")
    TbUser findByNameAndPwdAndRole(@Param("name")String userName,@Param("pwd")String passWord,@Param("role")Integer roleId);
    @Transactional
    @Query(nativeQuery = true,value = "update tb_user set tb_user.password=:pwd where tb_user.id=:userId")
    @Modifying
    int updatePwd(@Param("userId")Integer userId,@Param("pwd")String passWord);
    @Transactional
    @Query(nativeQuery = true,value = "update tb_user set tb_user.phone=:phone,tb_user.email=:email where tb_user.id=:id")
    @Modifying
    int updatePhoneEmail(@Param("phone")String phone,@Param("email")String email,@Param("id")Integer id);
    @Query(nativeQuery = true,value = "select u.* from tb_user u where u.id=:id")
    TbUser findUserById(@Param("id")Integer id);
    List<TbUser> findByRoleIdIn(List<Integer> roles);
}
