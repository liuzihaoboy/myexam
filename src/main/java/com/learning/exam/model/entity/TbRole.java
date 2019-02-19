package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liuzihao
 * @date 2019-01-05  16:59
 */
@Data
@Entity
public class TbRole {
    @Id
    @GeneratedValue
    private Integer id;
    private String roleName;
    private String basePer;
}
