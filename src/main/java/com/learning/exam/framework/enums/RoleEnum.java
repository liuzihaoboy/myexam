package com.learning.exam.framework.enums;

import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-01-31  16:17
 */
@Getter
public enum  RoleEnum {
    /**
     *
     */
    SuperAdmin(1,"超级管理员","1"),
    NormalAdmin(2,"管理员","2"),
    Teacher(3,"教师","2"),
    Student(4,"学生","2");
    RoleEnum(Integer id, String roleName, String basePer) {
        this.id = id;
        this.roleName = roleName;
        this.basePer = basePer;
    }
    private Integer id;
    private String roleName;
    private String basePer;
}
