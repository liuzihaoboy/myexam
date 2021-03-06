package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @author liuzihao
 * @date 2018-12-04 16:03
 */
@Data
@Entity
public class TbUser {
    @Id
    @GeneratedValue
    private Integer id;
    private String account;
    @Column(updatable = false)
    private String password;
    private String name;
    @Column(updatable = false)
    private String phone;
    @Column(updatable = false)
    private String email;
    private Integer roleId;
    private String permissions;
}
