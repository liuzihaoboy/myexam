package com.learning.exam.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author liuzihao
 * @date 2019-02-23  15:49
 */
@Data
public class PaperSectionDto {
    private Integer id;
    @NotNull(message = "试卷id不能为空")
    private Integer paperId;
    @NotNull(message = "章节类型不能为空")
    private Integer sectionType;
    @NotNull(message = "试题分数不能为空")
    private Integer questionScore;
    @NotNull(message = "试题数量不能为空")
    private Integer questionNum;
    @NotEmpty(message = "包含题库不能为空")
    private String qdbIds;
    @NotNull(message = "难度比例不能为空")
    private Integer[] levelScale;

}
