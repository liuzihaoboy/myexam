package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 题库实体
 * @author liuzihao
 * @date 2019-02-11  13:47
 */
@Data
@Entity
public class TbQuestionDb {
    @Id
    @GeneratedValue
    private Integer id;
    private String qdbName;
    private String qdbInfo;
    private Integer courseId;
    private Integer qdbStatus;
    private Date cDate;
    private Date uDate;
    private Integer cUid;
    private Integer uUid;

}
