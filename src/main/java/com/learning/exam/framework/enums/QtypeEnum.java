package com.learning.exam.framework.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-02-14  15:49
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum QtypeEnum {
    /**
     *
     */
    EXCLUSIVE_CHOICE(1,"单选题"),
    MULTIPLE_CHOICE(2,"多选题"),
    TRUE_FALSE(3,"判断题"),
    FILL_BLANKS(4,"填空题"),
    ;

    QtypeEnum(Integer id,String type) {
        this.id=id;
        this.type=type;
    }

    private Integer id;
    private String type;
}
