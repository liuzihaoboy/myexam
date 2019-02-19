package com.learning.exam.framework.advice;

import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.model.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuzihao
 * @date 2019-01-31  11:29
 */
@ControllerAdvice
@Slf4j
public class ValidationExceptionHandler {
    @ExceptionHandler(value = ValidationJsonException.class)
    @ResponseBody
    private JsonResult validationHandler(ValidationJsonException validationException){
        return JsonResult.error(validationException.getCodeMsg());
    }
    @ExceptionHandler(value = ValidationHtmlException.class)
    private String validationHandler(ValidationHtmlException validationException, HttpServletRequest request){
        request.setAttribute("errorMsg",validationException.getMessage());
        return "common/error";
    }
}
