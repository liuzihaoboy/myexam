package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-21  11:02
 */
@Data
public class TbPaper {
    @Id
    @GeneratedValue
    private Integer id;
    private String paperName;
    private Integer courseId;
    private Date startTime;
    private Date endTime;
    private Integer paperMinute;
    private Integer showKey;
    private Integer paperType;
    private String toUser;
    private Integer passScore;
}
