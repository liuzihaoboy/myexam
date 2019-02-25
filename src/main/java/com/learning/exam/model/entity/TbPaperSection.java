package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liuzihao
 * @date 2019-02-21  11:13
 */
@Data
@Entity
public class TbPaperSection {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer paperId;
    private Integer sectionType;
    private Integer questionNum;
    private Integer questionScore;
    private String qdbIds;
    private String levelScale;
}
