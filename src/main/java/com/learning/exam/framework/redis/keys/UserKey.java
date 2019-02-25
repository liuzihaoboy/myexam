package com.learning.exam.framework.redis.keys;

/**
 * @author liuzihao
 * @date 2019-01-31  12:54
 */
public class UserKey extends BasePrefix {
    public UserKey(String prefix) {
        super(prefix);
    }

    public UserKey(String prefix,int expireSeconds) {
        super(prefix,expireSeconds);
    }
    public static final UserKey userByPaperUserId = new UserKey("paperUser");
    public static final UserKey userByPaperTestId = new UserKey("paperTest");
}
