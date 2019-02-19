package com.learning.exam.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author liuzihao
 * @date 2019-01-03  12:48
 */
@Component
@PropertySource(value = "classpath:config/appConfig.properties")
@ConfigurationProperties(prefix = "question")
@Data
public class AppConfig {

}
