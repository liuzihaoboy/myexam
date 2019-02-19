package com.learning.exam.framework.advice;

import com.learning.exam.framework.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuzihao
 * @date 2019-02-02  21:52
 */
@ControllerAdvice
@Slf4j
public class AuthExceptionHandler {
    @ExceptionHandler(AuthException.class)
    public String authExceptionHandler(AuthException exception, HttpServletRequest request){
        request.setAttribute("errorMsg",exception.getCodeMsg().getMsg());
        return "common/appError";
    }
}
