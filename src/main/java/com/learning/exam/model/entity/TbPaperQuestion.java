package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liuzihao
 * @date 2019-02-21  11:15
 */
@Data
public class TbPaperQuestion {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer sectionId;
    private String questionIds;
}
