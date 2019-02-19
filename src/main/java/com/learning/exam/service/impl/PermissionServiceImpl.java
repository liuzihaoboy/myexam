package com.learning.exam.service.impl;

import com.learning.exam.dao.jpa.PermissionMenuJpa;
import com.learning.exam.model.entity.TbPermissionMenu;
import com.learning.exam.model.vo.BaseMenu;
import com.learning.exam.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuzihao
 * @date 2019-02-03  21:27
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMenuJpa permissionMenuJpa;
    @Override
    public List<BaseMenu> getPerMenus(Integer perId) {
        List<TbPermissionMenu> tbPermissionMenus = permissionMenuJpa.findAllByPerId(perId);
        List<BaseMenu> baseMenus = tbPermissionMenus.stream().map(permissionMenu ->{
            BaseMenu baseMenu = new BaseMenu();
            baseMenu.setUrl(permissionMenu.getMenuUrl());
            baseMenu.setTitle(permissionMenu.getMenuName());
            return baseMenu;
        }).collect(Collectors.toList());
        return baseMenus;
    }
}
