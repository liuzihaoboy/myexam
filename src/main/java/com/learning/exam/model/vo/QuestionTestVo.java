package com.learning.exam.model.vo;

import com.learning.exam.model.entity.TbQuestionOpt;
import lombok.Data;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-25  14:53
 */
@Data
public class QuestionTestVo {
    private Integer id;
    private String questionContent;
    private Integer questionType;
    private String questionTypeName;
    private Integer questionScore;
    private Integer blankNum;
    private String optKey;
    private List<QuestionOptVo> opts;
}
