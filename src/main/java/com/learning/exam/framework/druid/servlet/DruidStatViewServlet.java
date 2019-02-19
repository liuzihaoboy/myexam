package com.learning.exam.framework.druid.servlet;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * StatViewServlet
 *
 * @author 单红宇(365384722)
 * @myblog http://blog.csdn.net/catoop/
 * @create 2016年3月17日
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*",
    initParams={
            @WebInitParam(name="allow",value="127.0.0.1"),// IP白名单 (没有配置或者为空，则允许所有访问)
            @WebInitParam(name="deny",value=""),// IP黑名单 (存在共同时，deny优先于allow)
            @WebInitParam(name="loginUsername",value="admin"),// 用户名
            @WebInitParam(name="loginPassword",value="123456"),// 密码
            @WebInitParam(name="resetEnable",value="false")// 禁用HTML页面上的“Reset All”功能
    })
public class DruidStatViewServlet extends StatViewServlet {

}