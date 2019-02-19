package com.learning.exam.framework.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-02-14  16:01
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum  QlevelEnum {
    /**
     *
     */
    SIMPLE(1,"简单"),
    GENERAL(2,"普通"),
    MEDIUM(3,"中等"),
    LITTLE_DIFFICULT(4,"较难"),
    MORE_DIFFICULT(5,"困难")
    ;

    QlevelEnum(Integer id,String level) {
        this.id=id;
        this.level=level;
    }

    private Integer id;
    private String level;
}
