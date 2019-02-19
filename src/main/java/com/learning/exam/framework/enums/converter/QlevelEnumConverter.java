package com.learning.exam.framework.enums.converter;

import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.model.result.CodeMsg;

/**
 * @author liuzihao
 * @date 2019-02-14  19:24
 */
public class QlevelEnumConverter {
    public static QlevelEnum converter(Integer levelId){
        switch (levelId){
            case 1:return QlevelEnum.SIMPLE;
            case 2:return QlevelEnum.GENERAL;
            case 3:return QlevelEnum.MEDIUM;
            case 4:return QlevelEnum.LITTLE_DIFFICULT;
            case 5:return QlevelEnum.MORE_DIFFICULT;
            default:throw new ValidationJsonException(CodeMsg.Q_LEVEL_ERROR);
        }
    }
}
