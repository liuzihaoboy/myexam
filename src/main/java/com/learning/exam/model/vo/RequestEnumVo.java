package com.learning.exam.model.vo;

import lombok.Data;

/**
 * @author liuzihao
 * @date 2019-02-16  15:49
 */
@Data
public class RequestEnumVo {
    private Integer id;
    private String value;
    public RequestEnumVo(Integer id,String value){
        this.id=id;
        this.value=value;
    }
}
