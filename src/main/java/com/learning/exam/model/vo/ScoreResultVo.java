package com.learning.exam.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-03-04  11:08
 */
@Data
public class ScoreResultVo {
    private Integer userId;
    private Date startTime;
    private Date submitTime;
    private Integer resultScore;
    private Integer resultMinute;
    private String account;
    private String name;
    private String gradeName;
    private String majorName;
    private String classes;
}
