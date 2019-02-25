package com.learning.exam.framework.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-02-21  15:32
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaperTypeEnum {
    /**
     *
     */
    NORMAL_TYPE(1,"普通试卷"),
    RANDOM_TYPE(2,"随机试卷"),
    ;
    private Integer id;
    private String type;

    PaperTypeEnum(Integer id, String type) {
        this.id = id;
        this.type = type;
    }
}
