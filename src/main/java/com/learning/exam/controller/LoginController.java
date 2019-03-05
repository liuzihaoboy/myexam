package com.learning.exam.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.enums.converter.RoleEnumConverter;
import com.learning.exam.framework.exception.AuthException;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.model.dto.UserDto;
import com.learning.exam.model.entity.TbStudent;
import com.learning.exam.model.entity.TbUser;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.JsonResult;
import com.learning.exam.model.result.ViewUtils;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.UserService;
import com.learning.exam.util.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;

/**
 * 登录退出
 * @author liuzihao
 * @date 2019-01-03  13:24
 */
@Controller
@Slf4j
public class LoginController {
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;

    /**
     * 登录
     * @return html
     */
    @RequestMapping(value = "/login",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String login(HttpServletRequest request){
        HttpSession session = request.getSession();
        //查看是否登录过
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        if(tbUserVo != null){
            String loginSessionId = redisService.hget(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),SessionCacheName.SESSION_ID,String.class);
            if(!StringUtils.isEmpty(loginSessionId)
                    &&!session.getId().equals(loginSessionId)){
                request.setAttribute("errorMsg",CodeMsg.LOGIN_OTHER_ERROR.getMsg());
            }else {
                return "redirect:/index.html";
            }
        }
        return "login";
    }

    /**
     * 首页
     * @return html
     */
    @RequestMapping(value = "/index.html",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    public String index(){
        return "index";
    }

    /**
     * 退出登录
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/logout",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public JsonResult logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.LOGIN_USER,TbUserVo.class);
        if(tbUserVo != null){
            String loginSessionId = redisService.hget(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),SessionCacheName.SESSION_ID,String.class);
            if(!session.getId().equals(loginSessionId)){
                return JsonResult.error(CodeMsg.LOGIN_OTHER_ERROR);
            }else{
                redisService.deleteLoginSession(session.getId(),Integer.toString(tbUserVo.getId()));
            }
        }
        return JsonResult.success(null);
    }

    /**
     *提交登录
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/loginSubmit",method = {RequestMethod.POST},produces = "application/json;charset=utf-8")
    public JsonResult bsLogin(HttpServletRequest request,@Validated UserDto userDto){
        String verifyCode = userDto.getVerifyCode();
        HttpSession session = request.getSession();
        //验证码
        String cacheCode = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.VERIFY_CODE,String.class);
        if(!verifyCode.toLowerCase().equals(cacheCode.toLowerCase())){
            throw new ValidationJsonException(CodeMsg.VERIFY_ERROR);
        }
        RoleEnum roleEnum = RoleEnumConverter.converter(userDto.getRole());
        //验证用户
        TbUser tbUser = userService.getUser(userDto.getUserName(),userDto.getPassWord(),roleEnum);
        if(tbUser==null){
            throw new ValidationJsonException(CodeMsg.INFO_ERROR);
        }
        TbUserVo tbUserVo = userService.getUserVoFromTb(tbUser);
        //更新session信息
        String oldSessionId = redisService.hget(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),SessionCacheName.SESSION_ID,String.class);
        redisService.updateLoginSerssion(session.getId(),oldSessionId,tbUserVo);
        return JsonResult.success(tbUserVo);
    }
    @RequestMapping("/register.html")
    public String register(HttpServletRequest request){
        request.setAttribute("majors",userService.getStudentMajors());
        request.setAttribute("grades",userService.getStudentGrades());
        return "register";
    }
    @RequestMapping("/registerSubmit")
    @Transactional(rollbackFor = Exception.class)
    public String registerSubmit(@RequestParam("account")String account,
                                 @RequestParam("name")String name,
                                 @RequestParam("institute")String institute,
                                 @RequestParam("gradeId")Integer gradeId,
                                 @RequestParam("majorId")Integer majorId,
                                 @RequestParam("classes")String classes,
                                 @RequestParam("idCard")String idCard,
                                 @RequestParam("sex")String sex){
        Integer userId = userService.getUserIdByAccount(account);
        if(userId != null){
            throw new AuthException(CodeMsg.HAD_REGISTER);
        }
        TbUser tbUser = new TbUser();
        tbUser.setAccount(account);
        tbUser.setName(name);
        tbUser.setRoleId(RoleEnum.Student.getId());
        tbUser.setPermissions("2");
        tbUser.setPhone("无");
        tbUser.setEmail("无");
        tbUser.setPassword(Md5Utils.md5(account));
        userService.insertUser(tbUser);
        TbStudent tbStudent = new TbStudent();
        tbStudent.setUserId(tbUser.getId());
        tbStudent.setIcon("无");
        tbStudent.setInstitute(institute);
        tbStudent.setGradeId(gradeId);
        tbStudent.setMajorId(majorId);
        tbStudent.setClasses(classes);
        tbStudent.setIdCard(idCard);
        tbStudent.setSex(sex);
        userService.insertStudent(tbStudent);
        return ViewUtils.SUCCESS_PAGE;
    }
    /**
     * 验证码
     * 返回img
     */
    @RequestMapping(value = "/kaptcha",method = RequestMethod.GET)
    public void kaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException{
        byte[] responseImageBytes;
        ByteArrayOutputStream outputStream;
        try {
            //验证码
            String text = defaultKaptcha.createText();
            HttpSession session = httpServletRequest.getSession();
            redisService.hset(SessionKey.sessionById,session.getId(),SessionCacheName.VERIFY_CODE,text);
            if (SessionKey.sessionById.expireSeconds()>0){
                redisService.expire(SessionKey.sessionById,session.getId(),SessionKey.sessionById.expireSeconds());
            }
            BufferedImage image = defaultKaptcha.createImage(text);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image,"jpg",outputStream);
            responseImageBytes = outputStream.toByteArray();
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        httpServletResponse.setHeader("Cache-Control","no-store");
        httpServletResponse.setHeader("Pragma","no-cache");
        httpServletResponse.setDateHeader("Expires",0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        servletOutputStream.write(responseImageBytes);
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    /**
     * 下载文件
     * .xls
     */
    @RequestMapping(value = "/system/download/{filename}",method = RequestMethod.GET)
    public void download(@PathVariable("filename")String fileName, HttpServletResponse response) throws IOException{
        OutputStream out=null;
        InputStream in=null;
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName+".xls", "UTF-8"));
            in = this.getClass().getResourceAsStream("/file/"+fileName+".xls");
            byte[] buf = new byte[1024];
            out=new BufferedOutputStream(response.getOutputStream());
            while((in.read(buf))!=-1){
                out.write(buf);
            }
        }catch (Exception e){
            log.error("[导出excel文件]发送异常,filename={},错误内容={}",fileName,e.getMessage());
            throw new ValidationHtmlException("找不到文件");
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
                if(out!=null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
