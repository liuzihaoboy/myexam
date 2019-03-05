package com.learning.exam.model.vo;

import com.learning.exam.framework.enums.PaperTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-03-04  10:24
 */
@Data
public class ScoreVo {
    private Integer id;
    private String paperName;
    private Date startTime;
    private Date endTime;
    private Integer paperMinute;
    private Integer showKey;
    private Integer paperType;
    private String paperTypeName;
    private Integer passScore;
    private Integer totalScore;
    private String toUser;
    private Integer userNum;
    private Integer submitNum;
    private Integer maxScore;
    private Double aveScore;
    private Integer minScore;
}
