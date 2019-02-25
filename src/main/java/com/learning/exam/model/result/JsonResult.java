package com.learning.exam.model.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class JsonResult<T> implements Serializable{
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;
    /**
     * 返回结果 true or false
     */
    private boolean status;
    private Integer code;
    private String msg;
    private T data;
    public static <T> JsonResult<T> success(T data){
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setStatus(true);
        jsonResult.setCode(CodeMsg.SUCCESS.getCode());
        jsonResult.setMsg(CodeMsg.SUCCESS.getMsg());
        jsonResult.setData(data);
        return jsonResult;
    }
    public static <T> JsonResult<T> error(T data){
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setStatus(false);
        jsonResult.setCode(CodeMsg.SERVER_ERROR.getCode());
        jsonResult.setMsg(CodeMsg.SERVER_ERROR.getMsg());
        jsonResult.setData(data);
        return jsonResult;
    }
    public static JsonResult<String> error(CodeMsg codeMsg){
        JsonResult<String> jsonResult = new JsonResult<>();
        jsonResult.setStatus(false);
        jsonResult.setCode(codeMsg.getCode());
        jsonResult.setMsg(codeMsg.getMsg());
        jsonResult.setData(codeMsg.getMsg());
        return jsonResult;
    }
    public static <T> JsonResult<T> error(CodeMsg codeMsg,T data){
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setStatus(false);
        jsonResult.setCode(codeMsg.getCode());
        jsonResult.setMsg(codeMsg.getMsg());
        jsonResult.setData(data);
        return jsonResult;
    }
}
