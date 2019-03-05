package com.learning.exam.framework.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.enums.UploadTypEnum;
import com.learning.exam.model.entity.TbStudent;
import com.learning.exam.model.entity.TbUser;
import com.learning.exam.util.Md5Utils;
import com.learning.exam.util.SpringBeanUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-03-02  17:59
 */
@Slf4j
public class ExcelListener extends AnalysisEventListener {
    private ExcelObjectSave objectSave;
    private List<List<String>> datas;
    public ExcelListener(UploadTypEnum typEnum) {
        super();
        objectSave = ExcelSaveFactory.produce(typEnum);
        datas = new ArrayList<>();
    }

    @Override
    public void invoke(Object o, AnalysisContext context) {
        List<String> rows = (List<String>) o;
        if(context.getCurrentRowNum()>=1){
            datas.add(rows);
        }
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        objectSave.save(datas);
    }

    public List<List<String>> getDatas() {
        return datas;
    }

    public void setDatas(List<List<String>> datas) {
        this.datas = datas;
    }
}
