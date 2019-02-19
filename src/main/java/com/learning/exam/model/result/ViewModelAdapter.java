package com.learning.exam.model.result;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author liu_yeye
 * @date 2018-07-30 14:55
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ViewModelAdapter implements Serializable {

    /**
     * 返回结果 true or false
     */
    private boolean status;
    private Integer code;
    private String msg;
    private Object data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
