package com.learning.exam.service;

import com.learning.exam.framework.enums.UploadTypEnum;

/**
 * @author liuzihao
 * @date 2019-03-02  17:51
 */
public interface ExcelService {
    void readExcel(String filePath, Integer type);
}
