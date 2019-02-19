package com.learning.exam.framework.enums;

import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author liuzihao
 * @date 2019-02-03  20:56
 */
@Getter
public enum  NewsMenuEnum {
    /**
     *
     */
    NEWS_LIST("公告列表","/system/news/list"),
    C_LIST("公告分类","/system/news/category/list"),
    ;
    NewsMenuEnum(String title, String url){
        this.title=title;
        this.url=url;
    }
    private String title;
    private String url;
}
