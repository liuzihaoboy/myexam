package com.learning.exam.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-25  14:50
 */
@Data
public class SectionTestVo {
    private Integer sectionType;
    private String sectionTypeName;
    Integer questionNum;
    Integer questionScore;
    private String levelScale;
    private String qdbIds;
    List<QuestionTestVo> questions;
}
