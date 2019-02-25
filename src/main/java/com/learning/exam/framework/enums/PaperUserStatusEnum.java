package com.learning.exam.framework.enums;

import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-02-24  11:30
 */
@Getter
public enum  PaperUserStatusEnum {
    /**
     *
     */
    WAIT_START(1,"等待开始"),
    BE_IN(2,"正在进行"),
    HAD_SUBMIT(0,"已提交"),
    NO_JOIN(3,"未参加"),
    ;
    private Integer id;
    private String status;

    PaperUserStatusEnum(Integer id, String status) {
        this.id = id;
        this.status = status;
    }
}
