package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liuzihao
 * @date 2019-01-05  17:01
 */
@Data
@Entity
public class TbPermission {
    @Id
    @GeneratedValue
    private Integer id;
    private String perTitle;
    private String perUrl;
}
