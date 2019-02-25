package com.learning.exam.framework.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.learning.exam.framework.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.geo.Distance;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 配置类
 * @author sam
 * @date 2017/7/16
 */
@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private AppConfig appConfig;
    /**
     * 拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**").excludePathPatterns("/logout","/login","/loginSubmit","/kaptcha","/error");
        super.addInterceptors(registry);
    }

    /**
     * 路径映射配置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将所有/static/** 访问都映射到classpath:/static/ 目录下
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //registry.addResourceHandler("/static/weixin/**").addResourceLocations("file:"+dirPath+"/weixin/");
        super.addResourceHandlers(registry);
    }
    @Override
    public  void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = objectMapper();
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * 序列换成json时,将所有的long变成string
         * 因为js中得数字类型不能包含所有的java long值
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        //设置null值不参与序列化(字段不被显示)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(PropertyAccessor.FIELD,JsonAutoDetect.Visibility.ANY);
        objectMapper.setVisibility(PropertyAccessor.GETTER,JsonAutoDetect.Visibility.NONE);
        // 禁用空对象转换json校验
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //格式化时间
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy年M月dd日 HH:mm"));
        //objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return objectMapper;
    }
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;
    /**
     * 增加字符串转日期的功能
     */
    @PostConstruct
    public void initEditableValidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter
                .getWebBindingInitializer();
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer
                    .getConversionService();
            genericConversionService.addConverter(new StringToDateConverter());
        }
    }
    /**
     * 字符串转日期的转换器
     * @author byshome
     */
    class StringToDateConverter implements Converter<String, Date> {
        private static final String dateFormat = "yyyy-MM-dd HH:mm";
        private static final String shortDateFormat = "yyyy-MM-dd";
        /**
         * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
         */
        @Override
        public Date convert(String source) {
            if (StringUtils.isEmpty(source)) {
                return null;
            }
            source = source.trim();
            try {
                if (source.contains("-")) {
                    SimpleDateFormat formatter;
                    if (source.contains(":")) {
                        formatter = new SimpleDateFormat(dateFormat);
                    } else {
                        formatter = new SimpleDateFormat(shortDateFormat);
                    }
                    Date dtDate = formatter.parse(source);
                    return dtDate;
                } else if (source.matches("^\\d+$")) {
                    Long lDate = new Long(source);
                    return new Date(lDate);
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("parser %s to Date fail", source));
            }
            throw new RuntimeException(String.format("parser %s to Date fail", source));
        }
    }
}