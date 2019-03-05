package com.learning.exam.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learning.exam.util.DateTimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-03-01  11:21
 */
@Data
public class StudentPaperVo {
    private List<QuestionTestVo> questions;
    @JsonFormat(pattern = DateTimeUtils.HOUR_PATTERN_TYPE,timezone = "GMT+8")
    private Date endTime;
    private String saveKey;
}
