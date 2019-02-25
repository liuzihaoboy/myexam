package com.learning.exam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.exam.dao.jpa.QuestionJpa;
import com.learning.exam.dao.jpa.QuestionOptJpa;
import com.learning.exam.dao.mapper.QuestionDbMapper;
import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.entity.*;
import com.learning.exam.model.vo.MenuVo;
import com.learning.exam.service.QuestionService;
import com.learning.exam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExamApplicationTests {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private QuestionDbMapper questionDbMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private QuestionOptJpa questionOptJpa;
    @Autowired
    private QuestionJpa questionJpa;
    @Test
    public void contextLoads()  {
        String menuVoList=redisService.hget(SessionKey.sessionById,"D749BE63842B6B1D764BDBD062E85910",SessionCacheName.BASE_MENU,String.class);
        log.info(menuVoList+"*********");

        System.out.println(menuVoList.toString());
        //        String content = questionJpa.findQuestionContentById(1);
//        log.info("***************"+content+"***********");
//        TbQuestionOpt tbQuestionOpt = new TbQuestionOpt();
//        tbQuestionOpt.setQId(1);
//        tbQuestionOpt.setOrderNum(5);
//        tbQuestionOpt.setOptionContent("我是哈哈哈");
//        tbQuestionOpt = questionOptJpa.save(tbQuestionOpt);
//        log.info("************"+tbQuestionOpt.toString()+"*************");
//        Integer[] ids = questionOptJpa.findIdsByQId(1);
//        log.info(ids.toString());
//        List<TbQuestionDb> tbQuestionDbs = questionDbMapper.findQdbsByCondition("","",1,"0");
//        System.out.println(tbQuestionDbs);


//        HttpSession session = request.getSession();
//        MenuVo menuVo = new MenuVo();
//        menuVo.setTitle("题库管理");
//        List<BaseMenu> menus = new ArrayList<>();
//        BaseMenu baseMenu = new BaseMenu();
//        baseMenu.setTitle(QdbMenuEnum.QDB_LIST.getTitle());
//        baseMenu.setUrl(QdbMenuEnum.QDB_LIST.getUrl());
//        menus.add(baseMenu);
//        menuVo.setMenus(menus);
//        List<MenuVo> menuVos = new ArrayList<>();
//        menuVos.add(menuVo);
//        redisService.hset(SessionKey.sessionById,session.getId(),SessionCacheName.BASE_MENU,menuVos);
//        List<MenuVo> menuVoss = redisService.hget(SessionKey.sessionById,session.getId(),SessionCacheName.BASE_MENU,ArrayList.class);
//        System.out.println(menuVoss);
//        String url="/system/";
//        System.out.println(url.indexOf("/system"));
//        String s = url.replaceFirst("/system","");
//        System.out.println("*********"+s);
//        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,"BF8F1A663533A51F0F47DE75E153CF4C",SessionCacheName.LOGIN_USER,TbUserVo.class);
//        System.out.println(tbUserVo);
//        TbUserVo tb = new TbUserVo();
//        tb.setAccount("admin");
//        tb.setEmail("123456");
//        tb.setId(1);
//        tb.setPhone("123456");
//        TbRole tbRole = new TbRole();
//        tbRole.setId(1);
//        tbRole.setRoleName("超级管理员");
//        tbRole.setBasePer("1");
//        tb.setTbRole(tbRole);
//        List<TbPermission> tbPermissions = new ArrayList<>();
//        TbPermission tbPermission = new TbPermission();
//        tbPermission.setId(1);
//        tbPermission.setPerTitle("所有");
//        tbPermission.setPerUrl("/*");
//        tbPermissions.add(tbPermission);
//        tb.setTbPermissions(tbPermissions);
//        redisService.hset(SessionKey.sessionById,"key1","key",tb);
//       TbUserVo tbUserVo1 = redisService.hget(SessionKey.sessionById,"key1","key",TbUserVo.class);
//        System.out.println(tbUserVo1);
        //System.out.println(Md5Utils.md5("123456")+"***********************");
        //userService.updatePwd("admin","123456");
//        Map<String,Object> map = new HashMap<>();
//        map.put("id",123);
//        TbUserVo tb = new TbUserVo();
//        tb.setAccount("123456");
//        tb.setPassword("123456");
//        map.put("vo",tb);
//        redisService.hsetMap(SessionKey.sessionById,"j123456",map);
//        redisService.expire(SessionKey.sessionById,"j123456",SessionKey.sessionById.expireSeconds());
//        System.out.println();redisService.getExpire(SessionKey.sessionById,"j123456");
    }

}

