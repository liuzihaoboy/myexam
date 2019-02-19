package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liuzihao
 * @date 2019-01-05  17:26
 */
@Data
@Entity
public class TbStudent {
    @Id
    @GeneratedValue
    private Integer id;
    private String accountId;
    private String idCard;
    private String sex;
    private String grade;
    private String icon;
    private String phone;
    private String email;
    private String institute;
    private String classes;
    private String major;
}
