package com.learning.exam.model.vo;

import lombok.Data;

/**
 * @author liuzihao
 * @date 2019-02-24  12:30
 */
@Data
public class StudentVo {
    private Integer id;
    private Integer userId;
    private String accountId;
    private String name;
    private Integer gradeId;
    private String gradeName;
    private Integer majorId;
    private String majorName;
    private String classes;
    private String institute;
    private String idCard;
    private String sex;
    private String icon;
}
