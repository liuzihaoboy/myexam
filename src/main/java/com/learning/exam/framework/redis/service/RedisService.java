package com.learning.exam.framework.redis.service;

import com.learning.exam.framework.redis.keys.KeyPrefix;
import com.learning.exam.model.vo.TbUserVo;

import java.util.Map;

/**
 * @author liuzihao
 * @date 2018-12-27  12:23
 */
public interface RedisService {
    <T> void set(KeyPrefix prefix, String key, T t);
    <T> T get(KeyPrefix prefix, String key, Class<T> c);
    void hsetMap(KeyPrefix prefix, String key, Map<String,Object> valueMap);
    <T> void hset(KeyPrefix prefix,String key,String paramKey,T paramValue);
    <T> T hget(KeyPrefix prefix,String key,String paramKey,Class<T> c);
    void delete(KeyPrefix prefix,String key);
    boolean exists(KeyPrefix prefix, String key);
    void expire(KeyPrefix prefix,String key,int time);
    long getExpire(KeyPrefix prefix,String key);
    void updateLoginSerssion(String sessionId, String oldSessionId, TbUserVo tbUserVo);
    void deleteLoginSession(String sessionId, String userId);
}
