package com.learning.exam.framework.enums.converter;

import com.learning.exam.framework.enums.PaperUserStatusEnum;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.model.result.CodeMsg;

/**
 * @author liuzihao
 * @date 2019-02-24  11:37
 */
public class PaperUserStatusEnumConverter {
    public static PaperUserStatusEnum converter(Integer status){
        switch (status){
            case 0:return PaperUserStatusEnum.HAD_SUBMIT;
            case 1:return PaperUserStatusEnum.WAIT_START;
            case 2:return PaperUserStatusEnum.BE_IN;
            default:throw new ValidationJsonException(CodeMsg.PAPER_USER_STATUS_ERROR);
        }
    }
}
