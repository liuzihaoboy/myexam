package com.learning.exam.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learning.exam.model.entity.TbCourse;
import lombok.Data;

import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-11  15:39
 */
@Data
public class QuestionDbVo {
    private Integer id;
    private String qdbName;
    private String qdbInfo;
    private Integer qdbStatus;
    private TbCourse tbCourse;
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
}
