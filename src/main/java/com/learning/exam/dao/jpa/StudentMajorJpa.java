package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbStudentMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * 学生专业持久层jpa
 * @author liuzihao
 * @date 2019-02-24  12:38
 */
@Repository
public interface StudentMajorJpa extends JpaRepository<TbStudentMajor,Integer> {
    /**
     * 插入专业
     * @param majorName 专业名
     * @return 修改行数
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_student_major(major_name) values (:majorName)")
    int insertMajor(@Param("majorName") String majorName);

    /**
     * 修改专业名
     * @param majorName 专业名
     * @param id 专业id
     * @return 修改行数
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_student_major set major_name=:majorName where id=:id")
    int updateMajor(@Param("majorName") String majorName,@Param("id")Integer id);
}
