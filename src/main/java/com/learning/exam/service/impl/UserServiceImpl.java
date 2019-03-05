package com.learning.exam.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.learning.exam.dao.jpa.*;
import com.learning.exam.dao.mapper.StudentMapper;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.model.entity.*;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.vo.StudentVo;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.UserService;
import com.learning.exam.util.Md5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liuzihao
 * @date 2019-01-31  10:36
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserJpa userJpa;
    @Autowired
    private StudentJpa studentJpa;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentGradeJpa studentGradeJpa;
    @Autowired
    private StudentMajorJpa studentMajorJpa;
    @Autowired
    private RoleJpa roleJpa;
    @Autowired
    private PermissionJpa permissionJpa;

    @Override
    public List<TbUserVo> getTbUserVosByRole(String roleKey) {
        List<Integer> roles = new ArrayList<>();
        if(StringUtils.isEmpty(roleKey)){
            roles.add(2);
            roles.add(3);
        }else{
            roles.add(Integer.parseInt(roleKey));
        }
        List<TbUser> tbUsers = userJpa.findByRoleIdIn(roles);
        List<TbUserVo> tbUserVos = new ArrayList<>(tbUsers.size());
        for (TbUser tbUser:tbUsers){
            tbUserVos.add(getUserVoFromTb(tbUser));
        }
        return tbUserVos;
    }

    @Override
    public int updateMajor(String majorName, Integer id) {
        return studentMajorJpa.updateMajor(majorName,id);
    }

    @Override
    public int insertMajor(String majorName) {
        return studentMajorJpa.insertMajor(majorName);
    }

    @Override
    public List<TbStudentMajor> getMajors() {
        return studentMajorJpa.findAll();
    }

    @Override
    public TbStudentMajor getMajorById(Integer id) {
        return studentMajorJpa.findOne(id);
    }

    @Override
    public Integer updatePhoneEmail(String phone, String email,Integer id) {
        return userJpa.updatePhoneEmail(phone,email,id);
    }

    @Override
    public List<StudentVo> getStudentByIds(String ids) {
        List<Integer> studentIds = Arrays.stream(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<TbStudent> tbStudents = studentJpa.findByUserIdIn(studentIds);
        Map<String,Object> map = new HashMap<>(16);
        List<StudentVo> studentVos = new ArrayList<>();
        StudentVo studentVo = null;
        for (TbStudent tbStudent:tbStudents){
            studentVo = studentVo(tbStudent,map);
            if(studentVo != null){
                studentVos.add(studentVo);
            }
        }
        return studentVos;
    }

    @Override
    public List<TbStudentMajor> getStudentMajors() {
        return studentMajorJpa.findAll();
    }

    @Override
    public List<TbStudentGrade> getStudentGrades() {
        return studentGradeJpa.findAll();
    }

    @Override
    public List<StudentVo> getStudentsByCondition(String gradeKey, String majorKey) {
        List<TbStudent> tbStudents = studentMapper.findStudentsByCondition(gradeKey, majorKey);
        Map<String,Object> map = new HashMap<>(16);
        List<StudentVo> studentVos = new ArrayList<>();
        StudentVo studentVo = null;
        for (TbStudent tbStudent:tbStudents){
            studentVo = studentVo(tbStudent,map);
            if(studentVo != null){
                studentVos.add(studentVo);
            }
        }
        return studentVos;
    }

    @Override
    public StudentVo getStudentByUserId(Integer userId) {
        TbStudent tbStudent = studentJpa.findByUserId(userId);
        return studentVo(tbStudent,new HashMap<>());
    }

    @Override
    public List<StudentVo> getStudents() {
        List<TbStudent> tbStudents = studentJpa.findAll();
        Map<String,Object> map = new HashMap<>(32);
        List<StudentVo> studentVos = new ArrayList<>();
        StudentVo studentVo = null;
        for (TbStudent tbStudent:tbStudents){
            studentVo = studentVo(tbStudent,map);
            if(studentVo != null){
                studentVos.add(studentVo);
            }
        }
        return studentVos;
    }
    private StudentVo studentVo(TbStudent tbStudent,Map<String,Object> map){
        if(tbStudent == null){
            return null;
        }
        StudentVo studentVo = new StudentVo();
        BeanUtils.copyProperties(tbStudent,studentVo);
        TbUser tbUser = userJpa.findUserById(tbStudent.getUserId());
        if(tbUser==null){
            return null;
        }
        studentVo.setName(tbUser.getName());
        studentVo.setAccountId(tbUser.getAccount());
        TbStudentGrade tbStudentGrade = (TbStudentGrade) map.get("grade:"+tbStudent.getGradeId());
        if(tbStudentGrade==null){
            tbStudentGrade = studentGradeJpa.findOne(tbStudent.getGradeId());
            if(tbStudentGrade!=null){
                studentVo.setGradeName(tbStudentGrade.getGradeName());
                map.put("grade:"+tbStudent.getGradeId(),tbStudentGrade);
            }
        }else {
            studentVo.setGradeName(tbStudentGrade.getGradeName());
        }
        TbStudentMajor tbStudentMajor = (TbStudentMajor) map.get("major:"+tbStudent.getMajorId());
        if(tbStudentMajor==null){
            tbStudentMajor = studentMajorJpa.findOne(tbStudent.getMajorId());
            if(tbStudentMajor!=null){
                studentVo.setMajorName(tbStudentMajor.getMajorName());
                map.put("major:"+tbStudent.getMajorId(),tbStudentMajor);
            }
        }else {
            studentVo.setMajorName(tbStudentMajor.getMajorName());
        }
        return studentVo;
    }
    @Override
    public Integer updatePwd(Integer userId, String passWord) {
        String md5Pwd = Md5Utils.md5(passWord);
        return userJpa.updatePwd(userId,md5Pwd);
    }

    /**
     * 实体user对象返回userVo对象
     * @param tbUser
     * @return
     */
    @Override
    public TbUserVo getUserVoFromTb(TbUser tbUser) {
        TbRole tbRole = roleJpa.findById(tbUser.getRoleId());
        List<Integer> perIds = Arrays.stream(tbUser.getPermissions().split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> rolePerIds = Arrays.stream(tbRole.getBasePer().split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<TbPermission> pers = permissionJpa.findAllByIdIn(perIds);
        List<TbPermission> rolePers = permissionJpa.findAllByIdIn(rolePerIds);
        Set<TbPermission> perSet = new HashSet<>();
        perSet.addAll(pers);
        perSet.addAll(rolePers);
        TbUserVo tbUserVo = new TbUserVo();
        BeanUtils.copyProperties(tbUser,tbUserVo);
        if(tbRole.getId().equals(RoleEnum.Student.getId())){
            Map<String,Object> map = new HashMap<>();
            StudentVo studentVo = studentVo(studentJpa.findByUserId(tbUser.getId()),map);
            if(studentVo == null){
                throw new ValidationJsonException(CodeMsg.NO_STUDENT);
            }
            tbUserVo.setStudentVo(studentVo);
        }
        tbUserVo.setTbRole(tbRole);
        tbUserVo.setTbPermissions(new ArrayList<>(perSet));
        return tbUserVo;
    }

    @Override
    public TbUser getUser(Integer id) {
        TbUser tbUser = userJpa.findUserById(id);
        if(tbUser !=null){
            tbUser.setPassword(null);
        }
        return tbUser;
    }

    @Override
    public TbUser getUser(String userName, String passWord,RoleEnum roleEnum) {
        String md5Pwd = Md5Utils.md5(passWord);
        return userJpa.findByNameAndPwdAndRole(userName,md5Pwd,roleEnum.getId());
    }

}
