package com.learning.exam.framework.redis.keys;

/**
 * @author liuzihao
 * @date 2018-12-27  14:06
 */
public abstract class BasePrefix implements KeyPrefix {
    //默认0永不过期
    private int expireSeconds;
    private String prefix;
    public BasePrefix(String prefix){
        this(prefix,0);
    }
    public BasePrefix(String prefix,int expireSeconds){
        this.expireSeconds=expireSeconds;
        this.prefix=prefix;
    }
    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+prefix+":";
    }
}
