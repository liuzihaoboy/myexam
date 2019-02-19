package com.learning.exam.framework.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author liuzihao
 * @date 2018-12-07 16:03
 */
@Configuration
@PropertySource(value = "classpath:redis/redis-config.properties")
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisConfig {
    public RedisConfig() {
    }
    private String host;
    private int port;
    private String password;
    private int database;
    private int timeout;
    private Pool pool;
    @Data
    public static class Pool{
        public Pool() {
        }
        private int maxActive;
        private int maxIdle;
        private int maxWait;
        private int minIdle;
    }
}
