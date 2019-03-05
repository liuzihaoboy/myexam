package com.learning.exam.framework.excel;

import com.learning.exam.dao.jpa.StudentGradeJpa;
import com.learning.exam.dao.jpa.StudentJpa;
import com.learning.exam.dao.jpa.StudentMajorJpa;
import com.learning.exam.dao.jpa.UserJpa;
import com.learning.exam.dao.mapper.StudentMapper;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.model.entity.TbStudent;
import com.learning.exam.model.entity.TbStudentGrade;
import com.learning.exam.model.entity.TbStudentMajor;
import com.learning.exam.model.entity.TbUser;
import com.learning.exam.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-03-02  18:14
 */
@Component
public class ExcelStudentSave implements ExcelObjectSave{
    @Autowired
    private UserJpa userJpa;
    @Autowired
    private StudentJpa studentJpa;
    @Autowired
    private StudentGradeJpa studentGradeJpa;
    @Autowired
    private StudentMajorJpa studentMajorJpa;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(Object o) {
        //账号（学号）（默认密码同）	姓名	学院	年级(例:2015)	专业编号	班级	身份证	性别
        List<List<String>> datas = (List<List<String>>) o;
        int idx=1;
        try{
            for (List<String> rows :datas){
                TbUser tbUser = new TbUser();
                tbUser.setAccount(rows.get(0));
                tbUser.setName(rows.get(1));
                tbUser.setPassword(Md5Utils.md5(tbUser.getAccount()));
                tbUser.setRoleId(RoleEnum.Student.getId());
                tbUser.setPhone("无");
                tbUser.setEmail("无");
                tbUser.setPermissions("2");
                userJpa.save(tbUser);
                TbStudent tbStudent = new TbStudent();
                tbStudent.setInstitute(rows.get(2));
                TbStudentGrade tbStudentGrade = studentGradeJpa.findByGradeName(rows.get(3));
                if(tbStudentGrade == null){
                    tbStudentGrade = new TbStudentGrade();
                    tbStudentGrade.setGradeName(rows.get(3));
                    studentGradeJpa.save(tbStudentGrade);
                }
                tbStudent.setGradeId(tbStudentGrade.getId());
                Integer majorId =Integer.parseInt(rows.get(4));
                TbStudentMajor tbStudentMajor = studentMajorJpa.findOne(majorId);
                if(tbStudentMajor == null){
                    throw new ValidationHtmlException("第"+idx+"行，专业编号未找到");
                }
                tbStudent.setMajorId(majorId);
                tbStudent.setClasses(rows.get(5));
                tbStudent.setIdCard(rows.get(6));
                tbStudent.setSex(rows.get(7));
                tbStudent.setUserId(tbUser.getId());
                tbStudent.setIcon("无");
                studentJpa.save(tbStudent);
                ++idx;
            }
        }catch (Exception e){
            throw new ValidationHtmlException("第"+idx+"行出错："+e.getMessage());
        }
        return 0;
    }
}
