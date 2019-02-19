package com.learning.exam.model.dto;

import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.model.entity.TbQuestionOpt;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-14  18:46
 */
@Data
public class QuestionDto {
    private Integer id;
    @NotEmpty(message = "题干内容不能为空")
    private String qContent;
    @NotNull(message = "题库编号不能为空")
    private Integer qdbId;
    @NotNull(message = "题目类型不能为空")
    private Integer qType;
    @NotNull(message = "题目等级不能为空")
    private Integer qLevel;
    @NotNull(message = "题目状态不能为空")
    private Integer qStatus;
    @NotEmpty(message = "题目答案不能为空")
    private String optKey;
    @NotEmpty(message = "题目解析没有请填无")
    private String keyInfo;
    private String[] q_key;
    private String[] q_options;
    private String[] orderNum;
}
