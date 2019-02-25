package com.learning.exam.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-25  9:58
 */
@Data
public class PaperUserVo {
    private Integer id;
    private Integer userId;
    private Integer paperId;
    private Integer status;
    private String statusName;
    private String paperName;
    private Date startTime;
    private Date endTime;
    private Integer paperMinute;
    private Integer paperType;
    private String paperTypeName;
    private Integer passScore;
    private Integer totalScore;
}
