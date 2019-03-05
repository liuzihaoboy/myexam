package com.learning.exam.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-24  11:28
 */
@Data
@Entity
public class TbPaperResult {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer paperTestId;
    private String resultKeys;
    private Integer resultScore;
    private Integer resultMinute;
    private Date submitTime;
    private String questionScore;
}
