package com.learning.exam.framework.excel;

import com.learning.exam.dao.jpa.QuestionJpa;
import com.learning.exam.dao.jpa.QuestionOptJpa;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.framework.enums.converter.QtypeEnumConverter;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.model.entity.*;
import com.learning.exam.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuzihao
 * @date 2019-03-02  18:18
 */
@Component
public class ExcelQuestionSave implements ExcelObjectSave{
    @Autowired
    private QuestionJpa questionJpa;
    @Autowired
    private QuestionOptJpa questionOptJpa;

    @Override
    public int save(Object o) {
        //题库id	题目类型(单选：1，多选2，判断3，填空4)	题目难度（1，2，3，4，5）	题干	答案解析（没有填无）	答案（选项第几列）（判断(T/F)填空写答案，没有选项）	选项1	选项…
        List<List<String>> datas = (List<List<String>>) o;
        int idx=1;
        try{
            for (List<String> rows :datas){
                TbQuestion question = new TbQuestion();
                question.setQdbId(Integer.parseInt(rows.get(0)));
                QtypeEnum qtypeEnum = QtypeEnumConverter.converter(Integer.parseInt(rows.get(1)));
                question.setQType(qtypeEnum.getId());
                question.setQLevel(Integer.parseInt(rows.get(2)));
                question.setQContent(rows.get(3));
                question.setKeyInfo(rows.get(4));
                String key = rows.get(5);
                question.setOptKey(key);
                question.setQStatus(0);
                question.setUUid(1);
                question.setCUid(1);
                questionJpa.save(question);
                if(qtypeEnum == QtypeEnum.EXCLUSIVE_CHOICE||
                        qtypeEnum == QtypeEnum.MULTIPLE_CHOICE){
                    List<Integer> keys = Arrays.stream(key.split(",")).map(Integer::parseInt).collect(Collectors.toList());
                    List<String> optKey = new ArrayList<>();
                    for (int i = 6; i < rows.size(); i++) {
                        TbQuestionOpt tbQuestionOpt = new TbQuestionOpt();
                        tbQuestionOpt.setQId(question.getId());
                        tbQuestionOpt.setOptionContent(rows.get(i));
                        tbQuestionOpt.setOrderNum(i-5);
                        questionOptJpa.save(tbQuestionOpt);
                        if(keys.contains(i)){
                            optKey.add(String.valueOf(tbQuestionOpt.getId()));
                        }
                    }
                    questionJpa.updateOptKey(optKey.stream().collect(Collectors.joining(",")),question.getId());
                }
                ++idx;
            }
        }catch (Exception e){
            throw new ValidationHtmlException("第"+idx+"行出错："+e.getMessage());
        }
        return 0;
    }
}
