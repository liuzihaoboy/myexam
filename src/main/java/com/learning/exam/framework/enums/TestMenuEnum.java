package com.learning.exam.framework.enums;

import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-02-03  20:50
 */
@Getter
public enum  TestMenuEnum {
    /**
     *
     */
    PAPER_LIST("试卷列表","/system/test/list"),
    C_LIST("试卷分类","/system/test/category/list"),
    ;
    TestMenuEnum(String title, String url){
        this.title=title;
        this.url=url;
    }
    private String title;
    private String url;
}
