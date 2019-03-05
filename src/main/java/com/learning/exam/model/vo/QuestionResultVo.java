package com.learning.exam.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-03-02  13:14
 */
@Data
public class QuestionResultVo {
    private Integer id;
    private String questionContent;
    private Integer questionType;
    private String questionTypeName;
    private Integer questionScore;
    private String keyInfo;
    private String optKey;
    private Integer flag;
    private String userKey;
    private List<QuestionOptVo> opts;
}
