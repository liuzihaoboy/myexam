package com.learning.exam.service;

import com.learning.exam.model.entity.TbPermissionMenu;
import com.learning.exam.model.vo.BaseMenu;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-03  21:26
 */
public interface PermissionService {
    List<BaseMenu> getPerMenus(Integer perId);
}
