package com.learning.exam.framework.excel;

import com.learning.exam.framework.enums.UploadTypEnum;
import com.learning.exam.util.SpringBeanUtils;

/**
 * @author liuzihao
 * @date 2019-03-02  18:21
 */
public class ExcelSaveFactory {
    public static ExcelObjectSave produce(UploadTypEnum typEnum){
        switch (typEnum){
            case STUDENT_EXCEL:{
                return SpringBeanUtils.getBean(ExcelStudentSave.class);
            }
            case QUESTION_EXCEL:{
                return SpringBeanUtils.getBean(ExcelQuestionSave.class);
            }
            default:{
                return null;
            }
        }
    }
}
