package com.learning.exam.framework.enums.converter;

import com.learning.exam.framework.enums.UploadTypEnum;

/**
 * @author liuzihao
 * @date 2019-03-02  18:23
 */
public class UploadTypEnumConverter {
    public static UploadTypEnum converter(Integer id){
        switch (id){
            case 1:return UploadTypEnum.STUDENT_EXCEL;
            case 2:return UploadTypEnum.QUESTION_EXCEL;
            default:{
                throw new RuntimeException("上传类型错误");
            }
        }
    }
}
