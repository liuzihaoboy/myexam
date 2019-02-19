package com.learning.exam.framework.advice;

import com.learning.exam.model.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuzihao
 * @date 2019-01-31  11:03
 */
@ControllerAdvice
@Slf4j
public class BindExceptionHandler {
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public JsonResult handlerExceptionJson(BindException bindException){
        BindingResult bindingResult = bindException.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        return JsonResult.error(fieldError.getDefaultMessage());
    }
}
