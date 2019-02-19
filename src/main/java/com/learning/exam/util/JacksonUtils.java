package com.learning.exam.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.exam.framework.exception.ValidationHtmlException;

/**
 * @author liuzihao
 * @date 2019-02-17  15:35
 */
public class JacksonUtils {
    private static final ObjectMapper om = SpringBeanUtils.getBean(ObjectMapper.class);
    public static String toJsonAsString(Object o){
        try {
            return om.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ValidationHtmlException("格式化失败");
        }
    }
}
