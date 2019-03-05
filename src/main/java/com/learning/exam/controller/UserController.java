package com.learning.exam.controller;

import com.learning.exam.dao.jpa.PermissionJpa;
import com.learning.exam.dao.jpa.RoleJpa;
import com.learning.exam.dao.jpa.UserJpa;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.model.entity.TbCourse;
import com.learning.exam.model.entity.TbPermission;
import com.learning.exam.model.entity.TbRole;
import com.learning.exam.model.entity.TbUser;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.model.vo.StudentVo;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.UserService;
import com.learning.exam.util.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理
 * @author liuzihao
 * @date 2019-03-03  13:43
 */
@Slf4j
@RequestMapping("/system/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserJpa userJpa;
    @Autowired
    private PermissionJpa permissionJpa;

    /**
     * 用户提交
     * @return html
     */
    @RequestMapping(value = "/detail/submit",method = RequestMethod.POST,produces = "text/html;charset=utf-8")
    public String submit(@RequestParam(value = "id",required = false)String id,
                         @RequestParam("roleId")Integer roleId,
                         @RequestParam("account")String account,
                         @RequestParam("phone")String phone,
                         @RequestParam("email")String email,
                         @RequestParam("name")String name,
                         @RequestParam("permissions")String permissions,
                         @RequestParam(value = "password",required = false)String password) {
        TbUser tbUser = new TbUser();
        if(!StringUtils.isEmpty(id)&&!"0".equals(id)){
            tbUser.setId(Integer.parseInt(id));
        }
        tbUser.setAccount(account);
        tbUser.setName(name);
        tbUser.setRoleId(roleId);
        tbUser.setPhone(phone);
        tbUser.setEmail(email);
        tbUser.setPermissions(StringUtils.isEmpty(permissions)?"2":permissions);
        tbUser.setPassword(StringUtils.isEmpty(password)?Md5Utils.md5(account):Md5Utils.md5(password));
        userJpa.save(tbUser);
        return ViewUtils.SUCCESS_PAGE;
    }

    /**
     * 用户详细
     * @return html
     */
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String detail(HttpServletRequest request,
                         @PathVariable("id")Integer id){
        TbUser tbUser = userService.getUser(id);
        request.setAttribute("tbUser",tbUser);
        return "user/form";
    }

    /**
     * 用户权限
     * @return html
     */
    @RequestMapping(value = "/detail/per/{type}",method = RequestMethod.GET,produces="text/html;charset=utf-8")
    public String per(HttpServletRequest request,
                       @PathVariable("type")Integer type,
                       @RequestParam(required = false,value = "t")String pers){
        if(type!=0&&type!=1){
            throw new ValidationHtmlException("类型错误");
        }
        request.setAttribute("type",type);
        if(!StringUtils.isEmpty(pers)&&type==0){
            request.setAttribute("t",pers);
        }
        return "user/per";
    }

    /**
     * 用户权限
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/detail/per/json",method = {RequestMethod.POST},produces = "application/json;charset=utf-8")
    public List<TbPermission> perJson(HttpServletRequest request,
                                      @RequestParam(value = "type")Integer type,
                                      @RequestParam(required = false,value = "t")String ids){
        if(type==0){
            if(!StringUtils.isEmpty(ids)){
                return permissionJpa.findAllByIdIn(Arrays.stream(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
            }
            else {
                return null;
            }
        }
        return permissionJpa.findAll();
    }

    /**
     * 删除用户
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
     * 所有用户
     * @return html
     */
    @GetMapping("/list")
    public String list (){
        return "user/list";
    }

    /**
     * 所有用户
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/list/json",method = {RequestMethod.POST,RequestMethod.GET})
    public List<TbUserVo> listJson(HttpServletRequest request){
        String roleKey = request.getParameter("roleKey");
        return userService.getTbUserVosByRole(roleKey);
    }

    /**
     * 修改密码
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/update/pw/{userId}",method = {RequestMethod.POST})
    public JsonResult uodatePw(@PathVariable("userId")Integer userId,
                               @RequestParam("pw")String pw){
        userService.updatePwd(userId,pw);
        return JsonResult.success(null);
    }
}
