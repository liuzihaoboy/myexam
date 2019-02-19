package com.learning.exam.framework.redis.config;

import com.learning.exam.framework.redis.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author liuzihao
 * @date 2018-12-27  12:04
 */
@Component
public class RedisPoolFactory {
    @Autowired
    RedisConfig redisConfig;
    @Bean
    JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisConfig.getPool().getMaxIdle());
        config.setMinIdle(redisConfig.getPool().getMinIdle());
        config.setMaxTotal(redisConfig.getPool().getMaxActive());
        config.setMaxWaitMillis(redisConfig.getPool().getMaxWait()*1000);
        JedisPool jedisPool = new JedisPool(config,redisConfig.getHost(),redisConfig.getPort(),
                redisConfig.getTimeout()*1000,redisConfig.getPassword(),redisConfig.getDatabase());
        return jedisPool;
    }
}
