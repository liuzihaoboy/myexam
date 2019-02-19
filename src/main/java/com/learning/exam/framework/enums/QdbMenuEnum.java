package com.learning.exam.framework.enums;

import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-02-03  20:19
 */
@Getter
public enum QdbMenuEnum {
    /**
     *
     */
    C_LIST("科目列表","/system/qdb/course/list"),
    QDB_LIST("题库列表","/system/qdb/list"),
    Q_LIST("试题列表","/system/qdb/question/list"),
    ;
    QdbMenuEnum(String title, String url){
        this.title=title;
        this.url=url;
    }
    private String title;
    private String url;
}
