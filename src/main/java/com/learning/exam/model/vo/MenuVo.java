package com.learning.exam.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-03  20:18
 */
@Data
public class MenuVo {
    private String title;
    private List<BaseMenu> menus;
}
