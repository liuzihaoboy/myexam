package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 题目选项
 * @author liuzihao
 * @date 2019-02-11  13:51
 */
@Data
@Entity
public class TbQuestionOpt {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer qId;
    private String optionContent;
    private Integer orderNum;
}
