package com.learning.exam.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-25  18:58
 */
@Data
public class PaperResultVo {
    private Integer userId;
    private Integer paperId;
    private String paperName;
    private Date startTime;
    private Date endTime;
    private Integer paperMinute;
    private Integer passScore;
    private String questionIds;
    private Date testStartTime;
    private Integer resultScore;
    private Integer resultMinute;
    private Date submitTime;
    private String resultKeys;
    private String questionScore;
    private Integer totalScore;
    private Integer showKey;
}
