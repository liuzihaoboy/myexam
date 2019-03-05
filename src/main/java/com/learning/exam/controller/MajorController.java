package com.learning.exam.controller;

import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.model.entity.TbStudentMajor;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 专业管理
 * @author liuzihao
 * @date 2019-03-03  16:01
 */
@Controller
@Slf4j
@RequestMapping(value = "/system/user/student/major")
public class MajorController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/detail/submit",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String submit(@RequestParam(value = "id",required = false)Integer id,
                         @RequestParam(value = "majorName")String majorName) {
        if(StringUtils.isEmpty(majorName)){
            throw new ValidationHtmlException(CodeMsg.MAJOR_SELECT_ERROR);
        }
        if(id==null||id==0){
            userService.insertMajor(majorName);
        }else {
            userService.updateMajor(majorName,id);
        }
        return ViewUtils.SUCCESS_PAGE;
    }
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String detail(HttpServletRequest request,
                         @PathVariable("id")Integer id){
        TbStudentMajor major = userService.getMajorById(id);
        request.setAttribute("major",major);
        return "user/major/form";
    }
    @ResponseBody
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult delete(@PathVariable("id")String id){
        return JsonResult.success(null);
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String courseList(){
        return "user/major/list";
    }
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<TbStudentMajor> courseListJson(){
        return userService.getMajors();
    }
}
