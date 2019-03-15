package com.learning.exam.framework.redis.service.impl;

import com.alibaba.fastjson.JSON;
import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.redis.keys.KeyPrefix;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.keys.UserKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.vo.TbUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author liuzihao
 * @date 2018-12-27  12:23
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    public static final Charset UTF8_CHARSET=Charset.forName("utf-8");
    @Autowired
    private JedisPool jedisPool;

    @Override
    public <T> void set(KeyPrefix prefix, String key, T t) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=realKey(prefix,key);
            int expire = prefix.expireSeconds();
            if(expire <= 0){
                jedis.set(realKey.getBytes(UTF8_CHARSET),serialize(t));
            }else {
                jedis.setex(realKey.getBytes(UTF8_CHARSET),expire,serialize(t));
            }
        }finally {
            close(jedis);
        }
    }

    @Override
    public <T> void setex(KeyPrefix prefix, String key, T t, int expire) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=realKey(prefix,key);
            jedis.setex(realKey.getBytes(UTF8_CHARSET),expire,serialize(t));
        }finally {
            close(jedis);
        }
    }

    @Override
    public <T> T get(KeyPrefix prefix,String key, Class<T> c) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=realKey(prefix,key);
            byte[] bytes = jedis.get(realKey.getBytes(UTF8_CHARSET));
            return deserialize(bytes,c);
        }finally {
            close(jedis);
        }
    }

    @Override
    public void expire(KeyPrefix prefix, String key,int time) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = realKey(prefix,key);
            //s
            jedis.expire(realKey.getBytes(UTF8_CHARSET),time);
        }finally {
            close(jedis);
        }
    }

    @Override
    public long getExpire(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = realKey(prefix,key);
            //ms
            return jedis.pttl(realKey.getBytes(UTF8_CHARSET));
        }finally {
            close(jedis);
        }
    }

    @Override
    public void updateLoginSession(String sessionId, String oldSessionId, TbUserVo tbUserVo) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String userRealKey = realKey(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()));
            //String oldSessionRealKey = realKey(SessionKey.sessionById,oldSessionId);
            String newSessionRealKey = realKey(SessionKey.sessionById,sessionId);
            jedis.watch(userRealKey);
            Transaction transaction = jedis.multi();
            //旧的登录信息
            //jedis.del(oldSessionRealKey.getBytes(UTF8_CHARSET));
            transaction.hset(newSessionRealKey.getBytes(UTF8_CHARSET),SessionCacheName.LOGIN_USER.getBytes(UTF8_CHARSET),serialize(tbUserVo));
            if (SessionKey.sessionById.expireSeconds()>0){
                transaction.expire(newSessionRealKey.getBytes(UTF8_CHARSET),SessionKey.sessionById.expireSeconds());
            }
            transaction.hset(userRealKey.getBytes(UTF8_CHARSET),SessionCacheName.SESSION_ID.getBytes(UTF8_CHARSET),serialize(sessionId));
            if(SessionKey.sessionByUserId.expireSeconds() > 0){
                transaction.expire(userRealKey.getBytes(UTF8_CHARSET),SessionKey.sessionByUserId.expireSeconds());
            }
            transaction.exec();
            jedis.unwatch();
        }finally {
            close(jedis);
        }

    }

    @Override
    public void deleteLoginSession(String sessionId,String userId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String userRealKey = realKey(SessionKey.sessionByUserId,userId);
            String sessionRealKey = realKey(SessionKey.sessionById,sessionId);
            jedis.watch(userRealKey);
            Transaction transaction = jedis.multi();
            transaction.del(userRealKey.getBytes(UTF8_CHARSET));
            transaction.del(sessionRealKey.getBytes(UTF8_CHARSET));
            transaction.exec();
            jedis.unwatch();
        }finally {
            close(jedis);
        }
    }

    @Override
    public <T> void hset(KeyPrefix prefix, String key, String paramKey, T paramValue) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = realKey(prefix, key);
            jedis.hset(realKey.getBytes(UTF8_CHARSET),paramKey.getBytes(UTF8_CHARSET),serialize(paramValue));
        }finally {
            close(jedis);
        }
    }

    @Override
    public  void hsetMap(KeyPrefix prefix, String key, Map<String,Object> valueMap) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = realKey(prefix,key);
            byte[] bytes = realKey.getBytes(UTF8_CHARSET);
            jedis.watch(bytes);
            Transaction transaction = jedis.multi();
            for (Map.Entry<String,Object> entry:valueMap.entrySet()){
                transaction.hset(bytes,entry.getKey().getBytes(UTF8_CHARSET),serialize(entry.getValue()));
            }
            transaction.exec();
            jedis.unwatch();
        }finally {
            close(jedis);
        }
    }

    @Override
    public <T> T hget(KeyPrefix prefix, String key, String paramKey, Class<T> c) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = realKey(prefix, key);
            byte[] bytes = jedis.hget(realKey.getBytes(UTF8_CHARSET),paramKey.getBytes(UTF8_CHARSET));
            return deserialize(bytes,c);
        }finally {
            close(jedis);
        }
    }

    @Override
    public boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(realKey(prefix,key).getBytes(UTF8_CHARSET));
        }finally {
            close(jedis);
        }
    }

    @Override
    public void delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=realKey(prefix,key);
            if(realKey.endsWith("*")){
                Set<String> set = jedis.keys(realKey);
                if(set.size()>0){
                    String[] keys = new String[set.size()];
                    set.toArray(keys);
                    jedis.del(keys);
                }
            }else{
                jedis.del(realKey.getBytes(UTF8_CHARSET));
            }
        }finally {
            close(jedis);
        }
    }

    private String realKey(KeyPrefix prefix, String key){
        return prefix.getPrefix()+key;
    }
    private <T> byte[] serialize(T t) {
        if(t==null){
            return new byte[0];
        }
        return JSON.toJSONString(t).getBytes(UTF8_CHARSET);
    }
    private <T> T deserialize(byte[] bytes,Class<T> c){
        if(bytes==null||bytes.length<=0){
            return null;
        }
        return JSON.parseObject(new String(bytes,UTF8_CHARSET),c);
    }
    private void close(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }
}
