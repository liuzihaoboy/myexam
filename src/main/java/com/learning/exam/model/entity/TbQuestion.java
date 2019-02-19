package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;



/**
 * 题目实体
 * @author liuzihao
 * @date 2019-02-11  13:43
 */
@Data
@Entity
public class TbQuestion {
    @Id
    @GeneratedValue
    private Integer id;
    private String qContent;
    private Integer qdbId;
    private Integer qType;
    private Integer qLevel;
    private Integer qStatus;
    private Date cDate;
    private Date uDate;
    private Integer cUid;
    private Integer uUid;
    private String optKey;
    private String keyInfo;
}
