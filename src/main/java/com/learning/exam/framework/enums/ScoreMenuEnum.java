package com.learning.exam.framework.enums;

import lombok.Getter;

/**
 * @author liuzihao
 * @date 2019-02-03  20:54
 */
@Getter
public enum  ScoreMenuEnum {
    /**
     *
     */
    SCORE_LIST("成绩分析","/system/score/list"),
    ;
    ScoreMenuEnum(String title, String url){
        this.title=title;
        this.url=url;
    }
    private String title;
    private String url;
}
