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
 * 用户持久层jpa
 * @author liuzihao
 * @date 2019-01-31  10:29
 */
@Repository
public interface UserJpa extends JpaRepository<TbUser,Integer> {
    /**
     * 获取用户实体
     * @param userName 用户账户
     * @param passWord 用户密码
     * @param roleId 角色id
     * @return 用户
     */
    @Query(nativeQuery = true,value = "select u.* from tb_user u where u.account=:name and u.password=:pwd and u.role_id=:role")
    TbUser findByNameAndPwdAndRole(@Param("name")String userName,@Param("pwd")String passWord,@Param("role")Integer roleId);

    /**
     * 修改密码
     * @param userId 用户id
     * @param passWord 密码
     * @return 修改行数
     */
    @Transactional
    @Query(nativeQuery = true,value = "update tb_user set tb_user.password=:pwd where tb_user.id=:userId")
    @Modifying
    int updatePwd(@Param("userId")Integer userId,@Param("pwd")String passWord);

    /**
     * 修改用户手机和邮箱
     * @param phone 手机
     * @param email 邮箱
     * @param id 用户id
     * @return 修改行数
     */
    @Transactional
    @Query(nativeQuery = true,value = "update tb_user set tb_user.phone=:phone,tb_user.email=:email where tb_user.id=:id")
    @Modifying
    int updatePhoneEmail(@Param("phone")String phone,@Param("email")String email,@Param("id")Integer id);

    /**
     * 获取用户实体
     * @param id 用户id
     * @return 用户
     */
    @Query(nativeQuery = true,value = "select u.* from tb_user u where u.id=:id")
    TbUser findUserById(@Param("id")Integer id);

    /**
     * 获取角色所有用户
     * @param roles 角色id list
     * @return 用户list
     */
    List<TbUser> findByRoleIdIn(List<Integer> roles);
}
