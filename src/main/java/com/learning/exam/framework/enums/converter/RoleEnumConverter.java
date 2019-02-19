package com.learning.exam.framework.enums.converter;

import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.model.result.CodeMsg;

/**
 * @author liuzihao
 * @date 2019-01-31  16:21
 */
public class RoleEnumConverter {
    public static RoleEnum converter(Integer roleId){
        switch (roleId){
            case 1:return RoleEnum.SuperAdmin;
            case 2:return RoleEnum.NormalAdmin;
            case 3:return RoleEnum.Teacher;
            case 4:return RoleEnum.Student;
            default:throw new ValidationJsonException(CodeMsg.ROLE_TYPE_ERROR);
        }
    }
}
