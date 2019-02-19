package com.learning.exam.framework.redis.keys;

/**
 * @author liuzihao
 * @date 2018-12-27  14:00
 */
public interface KeyPrefix {
    int expireSeconds();
    String getPrefix();
}
