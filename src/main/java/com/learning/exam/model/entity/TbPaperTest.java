package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-24  11:27
 */
@Data
@Entity
public class TbPaperTest {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer paperUserId;
    private String questionIds;
    private Date startTime;
}
