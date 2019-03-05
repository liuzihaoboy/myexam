package com.learning.exam.framework.redis.keys;

/**
 * @author liuzihao
 * @date 2019-02-26  10:14
 */
public class PaperResultKey extends BasePrefix {
    public PaperResultKey(String prefix) {
        super(prefix);
    }

    public PaperResultKey(String prefix,int expireSeconds) {
        super(prefix,expireSeconds);
    }
    public static final PaperResultKey resultByPaperUserId = new PaperResultKey("paperUserId",86400);
}
