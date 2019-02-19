package com.learning.exam.framework.redis.keys;

/**
 * @author liuzihao
 * @date 2019-01-25  11:42
 */
public class SessionKey extends BasePrefix {
    public SessionKey(String prefix) {
        super(prefix);
    }

    public SessionKey(String prefix,int expireSeconds) {
        super(prefix,expireSeconds);
    }
    public static final SessionKey sessionById = new SessionKey("id",1800);
    public static final SessionKey sessionByUserId = new SessionKey("userId",1800);
}
