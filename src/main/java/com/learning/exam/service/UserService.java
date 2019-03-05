package com.learning.exam.service;

import com.learning.exam.dao.jpa.UserJpa;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.model.entity.TbStudent;
import com.learning.exam.model.entity.TbStudentGrade;
import com.learning.exam.model.entity.TbStudentMajor;
import com.learning.exam.model.entity.TbUser;
import com.learning.exam.model.vo.StudentVo;
import com.learning.exam.model.vo.TbUserVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-01-31  10:34
 */
public interface UserService {
    TbUser insertUser(TbUser tbUser);
    TbStudent insertStudent(TbStudent tbStudent);
    List<TbUserVo> getTbUserVosByRole(String roleKey);
    int updateMajor(String majorName,Integer id);
    int insertMajor(String majorName);
    List<TbStudentMajor> getMajors();
    TbStudentMajor getMajorById(Integer id);
    Integer updatePhoneEmail(String phone,String email,Integer id);
    List<StudentVo> getStudentByIds(String ids);
    List<TbStudentMajor> getStudentMajors();
    List<TbStudentGrade> getStudentGrades();
    List<StudentVo> getStudentsByCondition(String gradeKey,String majorKey);
    StudentVo getStudentByUserId(Integer userId);
    List<StudentVo> getStudents();
    Integer updatePwd (Integer userId,String passWord);
    TbUserVo getUserVoFromTb(TbUser tbUser);
    TbUser getUser(Integer id);
    Integer getUserIdByAccount(String userName);
    TbUser getUser(String userName,String passWord,RoleEnum roleEnum);
}
