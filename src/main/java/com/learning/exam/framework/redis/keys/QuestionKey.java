package com.learning.exam.framework.redis.keys;

/**
 * @author liuzihao
 * @date 2019-02-25  16:04
 */
public class QuestionKey extends BasePrefix{
    public QuestionKey(String prefix) {
        super(prefix);
    }

    public QuestionKey(String prefix,int expireSeconds) {
        super(prefix,expireSeconds);
    }
    public static final QuestionKey questionById = new QuestionKey("id",86400);
}
