package com.learning.exam.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author liuzihao
 * @date 2019-02-11  14:11
 */
@Data
public class QdbDto {
    private Integer id;
    @NotEmpty(message = "题库名不能为空")
    private String qdbName;
    @NotEmpty(message = "题库说明不能为空")
    private String qdbInfo;
    @NotNull(message = "题库状态不能为空")
    private Integer qdbStatus;
    @NotNull(message = "题库课程编号不能为空")
    private Integer courseId;
}
