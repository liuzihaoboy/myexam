package com.learning.exam.framework.enums;

import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-03-02  17:53
 */
@Getter
public enum  UploadTypEnum {
    /**
     *
     */
    STUDENT_EXCEL(1,"上传学生"),
    QUESTION_EXCEL(2,"上传题目"),
    ;
    private int id;
    private String type;

    UploadTypEnum(int id, String type) {
        this.id = id;
        this.type = type;
    }
}
