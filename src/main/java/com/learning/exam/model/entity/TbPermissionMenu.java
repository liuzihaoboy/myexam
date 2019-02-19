package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liuzihao
 * @date 2019-02-03  21:23
 */
@Data
@Entity
public class TbPermissionMenu {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer perId;
    private String menuName;
    private String menuUrl;
}
