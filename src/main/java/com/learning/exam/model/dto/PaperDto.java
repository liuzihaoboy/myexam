package com.learning.exam.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-22  13:09
 */
@Data
public class PaperDto {
    private Integer id;
    @NotEmpty(message = "试卷名不能为空")
    private String paperName;
    @NotNull(message = "课程编号不能为空")
    private Integer courseId;
    @NotNull(message = "开始时间不能为空")
    private Date startTime;
    @NotNull(message = "开始时间不能为空")
    private Date endTime;
    @NotNull(message = "考试时长不能为空")
    private Integer paperMinute;
    @NotNull(message = "公布答案不能为空")
    private Integer showKey;
    @NotNull(message = "试卷类型不能为空")
    private Integer paperType;
    @NotNull(message = "及格分数不能为空")
    private Integer passScore;
    @NotEmpty(message = "目标用户不能为空")
    private String toUserIds;
    @NotNull(message = "试题配置不能为空")
    private Integer configStatus;
}
