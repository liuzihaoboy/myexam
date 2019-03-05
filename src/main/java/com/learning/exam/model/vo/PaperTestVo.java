package com.learning.exam.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-25  14:45
 */
@Data
public class PaperTestVo {
    private Integer paperId;
    private String paperName;
    private Date startTime;
    private Date endTime;
    private Integer paperMinute;
    private Integer showKey;
    private Integer paperType;
    private String paperTypeName;
    private Integer passScore;
    private Integer totalScore;
    private String uuid;
    private String questionIds;
    private List<SectionTestVo> sections;
}
