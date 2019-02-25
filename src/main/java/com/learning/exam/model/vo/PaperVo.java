package com.learning.exam.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learning.exam.framework.enums.PaperTypeEnum;
import com.learning.exam.model.entity.TbCourse;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-21  14:51
 */
@Data
public class PaperVo {
    private Integer id;
    private String paperName;
    private Integer courseId;
    private String courseName;
    private String startTime;
    private String endTime;
    private Integer paperMinute;
    private Integer showKey;
    private PaperTypeEnum paperType;
    private Integer passScore;
    private Integer totalScore;
    private Integer configStatus;
    private List<Integer> toUserIds;
    private String toUser;
    private Integer userNum;
    @JsonProperty("cDate")
    private Date cDate;
    @JsonProperty("cUserName")
    private String cUserName;
    private List<PaperSectionVo> sectionVos;
}
