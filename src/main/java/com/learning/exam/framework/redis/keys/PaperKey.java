package com.learning.exam.framework.redis.keys;

/**
 * @author liuzihao
 * @date 2019-02-25  13:40
 */
public class PaperKey extends BasePrefix {
    public PaperKey(String prefix) {
        super(prefix);
    }

    public PaperKey(String prefix,int expireSeconds) {
        super(prefix,expireSeconds);
    }
    public static final PaperKey paperById = new PaperKey("id");
}
