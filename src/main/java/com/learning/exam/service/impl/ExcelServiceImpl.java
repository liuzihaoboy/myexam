package com.learning.exam.service.impl;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.learning.exam.framework.enums.UploadTypEnum;
import com.learning.exam.framework.enums.converter.UploadTypEnumConverter;
import com.learning.exam.framework.excel.ExcelListener;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.service.ExcelService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-03-02  17:51
 */
@Service
public class ExcelServiceImpl implements ExcelService {
    @Override
    public void readExcel(String filePath, Integer type) {
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            UploadTypEnum typEnum = UploadTypEnumConverter.converter(type);
            ExcelListener excelListener = new ExcelListener(typEnum);
            ExcelReader reader = null;
            if(filePath.endsWith(ExcelTypeEnum.XLS.getValue())){
                reader = new ExcelReader(inputStream,ExcelTypeEnum.XLS,null,excelListener,true);
            }
            if(reader == null){
                throw new ValidationHtmlException("格式不支持,请上传xls类型");
            }
            reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
