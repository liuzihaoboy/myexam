package com.learning.exam.framework.enums;

import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-02-03  20:53
 */
@Getter
public enum  UserMenuEnum {
    /**
     *
     */
    USER_LIST("用户列表","/system/user/list"),
    ;
    UserMenuEnum(String title, String url){
        this.title=title;
        this.url=url;
    }
    private String title;
    private String url;
}
