package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liuzihao
 * @date 2019-02-24  10:57
 */
@Data
@Entity
public class TbPaperUser {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer userId;
    private Integer paperId;
    private Integer status;
}
