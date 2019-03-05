package com.learning.exam.controller;

import com.learning.exam.dao.jpa.PermissionJpa;
import com.learning.exam.dao.jpa.StudentJpa;
import com.learning.exam.dao.jpa.UserJpa;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.model.entity.TbPermission;
import com.learning.exam.model.entity.TbStudent;
import com.learning.exam.model.entity.TbUser;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.model.vo.StudentVo;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.ExcelService;
import com.learning.exam.service.UserService;
import com.learning.exam.util.FileUtils;
import com.learning.exam.util.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生管理
 * @author liuzihao
 * @date 2019-03-04  14:25
 */
@Slf4j
@RequestMapping("/system/user/student")
@Controller
public class UserStudentController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserJpa userJpa;
    @Autowired
    private StudentJpa studentJpa;
    @Autowired
    private ExcelService excelService;

    /**
     * 学生提交
     * @return html
     */
    @Transactional
    @RequestMapping(value = "/detail/submit",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String submit(@RequestParam(value = "userId",required = false)String userId,
                         @RequestParam("account")String account,
                         @RequestParam("name")String name,
                         @RequestParam("institute")String institute,
                         @RequestParam("gradeId")Integer gradeId,
                         @RequestParam("majorId")Integer majorId,
                         @RequestParam("classes")String classes,
                         @RequestParam("idCard")String idCard,
                         @RequestParam("sex")String sex){
        TbUser tbUser = new TbUser();
        TbStudent tbStudent = new TbStudent();
        if(!StringUtils.isEmpty(userId)){
            tbUser = userJpa.findUserById(Integer.parseInt(userId));
            if(tbUser == null){
                throw new ValidationHtmlException(CodeMsg.NO_STUDENT);
            }
            tbStudent = studentJpa.findByUserId(Integer.parseInt(userId));
            if(tbStudent == null){
                throw new ValidationHtmlException(CodeMsg.NO_STUDENT);
            }
        }else{
            //默认
            tbUser.setPhone("无");
            tbUser.setEmail("无");
            tbUser.setPassword(Md5Utils.md5(account));
        }
        tbUser.setAccount(account);
        tbUser.setName(name);
        tbUser.setRoleId(RoleEnum.Student.getId());
        tbUser.setPermissions("2");
        userJpa.save(tbUser);
        tbStudent.setUserId(tbUser.getId());
        tbStudent.setIcon("无");
        tbStudent.setInstitute(institute);
        tbStudent.setGradeId(gradeId);
        tbStudent.setMajorId(majorId);
        tbStudent.setClasses(classes);
        tbStudent.setIdCard(idCard);
        tbStudent.setSex(sex);
        studentJpa.save(tbStudent);
        return ViewUtils.SUCCESS_PAGE;
    }

    /**
     * 学生详细
     * @return html
     */
    @RequestMapping(value = "/detail/{userId}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String detail(HttpServletRequest request,
                         @PathVariable("userId")Integer userId){
        StudentVo studentVo = userService.getStudentByUserId(userId);
        request.setAttribute("studentVo",studentVo);
        request.setAttribute("majors",userService.getStudentMajors());
        request.setAttribute("grades",userService.getStudentGrades());
        return "user/student/form";
    }

    /**
     * 删除学生
     * @return json
     */
    @ResponseBody
    @Transactional
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult delete(@PathVariable("id")String id){
        if(id.contains(",")){
            String[] ids = id.split(",");
            for (String s:ids){
                userJpa.delete(Integer.parseInt(s));
            }
        }else{
            userJpa.delete(Integer.parseInt(id));
        }
        return JsonResult.success(null);
    }

    /**
     * 所有学生
     * @return html
     */
    @GetMapping("/list")
    public String list (HttpServletRequest request){
        request.setAttribute("majors",userService.getStudentMajors());
        request.setAttribute("grades",userService.getStudentGrades());
        return "user/student/list";
    }

    /**
     * 所有学生
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.POST,RequestMethod.GET})
    public List<StudentVo> listJson(HttpServletRequest request){
        String gradeKey = request.getParameter("gradeKey");
        String majorKey = request.getParameter("majorKey");
        List<StudentVo> studentVos = null;
        if(StringUtils.isEmpty(gradeKey)&&StringUtils.isEmpty(majorKey)){
            studentVos = userService.getStudents();
        }else {
            studentVos = userService.getStudentsByCondition(gradeKey,majorKey);
        }
        return studentVos;
    }

    /**
     * 上传学生
     * @return html
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String upload(HttpServletRequest request,
                         @RequestParam("file") MultipartFile file){
        String path = FileUtils.saveFile(file,"/upload");
        if(path == null){
            return ViewUtils.ERROR_PAGE;
        }
        excelService.readExcel(path,1);
        return ViewUtils.SUCCESS_PAGE;
    }
}
