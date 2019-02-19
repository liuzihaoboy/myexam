package com.learning.exam.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.learning.exam.dao.jpa.PermissionJpa;
import com.learning.exam.dao.jpa.RoleJpa;
import com.learning.exam.dao.jpa.UserJpa;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.model.entity.TbPermission;
import com.learning.exam.model.entity.TbRole;
import com.learning.exam.model.entity.TbUser;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.UserService;
import com.learning.exam.util.Md5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    private RoleJpa roleJpa;
    @Autowired
    private PermissionJpa permissionJpa;

    @Override
    public Integer updatePwd(String userName, String passWord) {
        String md5Pwd = Md5Utils.md5(passWord);
        return userJpa.updatePwd(userName,md5Pwd);
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
        tbUserVo.setTbRole(tbRole);
        tbUserVo.setTbPermissions(new ArrayList<>(perSet));
        return tbUserVo;
    }

    @Override
    public TbUser getUser(String userName, String passWord,RoleEnum roleEnum) {
        String md5Pwd = Md5Utils.md5(passWord);
        return userJpa.findByNameAndPwdAndRole(userName,md5Pwd,roleEnum.getId());
    }

}
