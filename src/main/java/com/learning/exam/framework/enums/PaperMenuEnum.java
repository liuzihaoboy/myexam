package com.learning.exam.framework.enums;

import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-02-03  20:50
 */
@Getter
public enum PaperMenuEnum {
    /**
     *
     */
    PAPER_LIST("试卷列表","/system/paper/list"),
    ;
    PaperMenuEnum(String title, String url){
        this.title=title;
        this.url=url;
    }
    private String title;
    private String url;
}
