package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liuzihao
 * @date 2019-02-11  13:42
 */
@Data
@Entity
public class TbCourse {
    @Id
    @GeneratedValue
    private Integer id;
    private String courseName;
}
