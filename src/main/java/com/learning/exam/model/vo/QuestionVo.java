package com.learning.exam.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.model.entity.TbQuestionOpt;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-14  16:09
 */
@Data
public class QuestionVo {
    private Integer id;
    private String qContent;
    private Integer qdbId;
    private String qdbName;
    @JsonProperty("qType")
    private QtypeEnum qType;
    @JsonProperty("qLevel")
    private QlevelEnum qLevel;
    @JsonProperty("qStatus")
    private Integer qStatus;
    @JsonProperty("cDate")
    private Date cDate;
    @JsonProperty("uDate")
    private Date uDate;
    @JsonProperty("cUid")
    private Integer cUid;
    @JsonProperty("uUid")
    private Integer uUid;
    @JsonProperty("cUserName")
    private String cUserName;
    @JsonProperty("uUserName")
    private String uUserName;
    private List<TbQuestionOpt> opts;
    private String[] optKey;
    private String keyInfo;
}
