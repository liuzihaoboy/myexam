package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liuzihao
 * @date 2019-02-24  12:29
 */
@Data
@Entity
public class TbStudentMajor {
    @Id
    @GeneratedValue
    private Integer id;
    private String majorName;
}
