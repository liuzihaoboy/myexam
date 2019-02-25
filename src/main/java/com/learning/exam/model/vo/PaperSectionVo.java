package com.learning.exam.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-21  14:56
 */
@Data
public class PaperSectionVo {
    private Integer id;
    private Integer sectionTypeId;
    private String sectionTypeName;
    private Integer questionNum;
    private Integer questionScore;
    private Integer totalScore;
    private Integer paperId;
    private Integer paperScore;
    private Integer paperType;
    private String levelScale;
    private List<Integer> levelScales;
    private List<QuestionDbNameVo> qdbs;
}
