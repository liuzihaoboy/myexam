package com.learning.exam.framework.enums.converter;

import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.model.result.CodeMsg;

/**
 * @author liuzihao
 * @date 2019-02-14  19:20
 */
public class QtypeEnumConverter {
    public static QtypeEnum converter(Integer typeId){
        switch (typeId){
            case 1:return QtypeEnum.EXCLUSIVE_CHOICE;
            case 2:return QtypeEnum.MULTIPLE_CHOICE;
            case 3:return QtypeEnum.TRUE_FALSE;
            case 4:return QtypeEnum.FILL_BLANKS;
            default:throw new ValidationJsonException(CodeMsg.Q_TYPE_ERROR);
        }
    }
}
