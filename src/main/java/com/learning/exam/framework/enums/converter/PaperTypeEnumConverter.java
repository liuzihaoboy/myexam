package com.learning.exam.framework.enums.converter;

import com.learning.exam.framework.enums.PaperTypeEnum;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.model.result.CodeMsg;

/**
 * @author liuzihao
 * @date 2019-02-21  15:34
 */
public class PaperTypeEnumConverter {
    public static PaperTypeEnum converter(Integer id){
        switch (id){
            case 1:{
                return PaperTypeEnum.NORMAL_TYPE;
            }
            case 2:{
                return PaperTypeEnum.RANDOM_TYPE;
            }
            default:{
                throw new ValidationJsonException(CodeMsg.PAPER_TYPE_ERROR);
            }
        }
    }
}
