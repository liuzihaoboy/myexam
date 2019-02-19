package com.learning.exam.framework.exception;

import com.learning.exam.model.result.CodeMsg;

/**
 * @author liuzihao
 * @date 2019-02-02  21:50
 */
public class AuthException extends RuntimeException {
    private CodeMsg codeMsg;

    public AuthException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.codeMsg=codeMsg;
    }

    public AuthException(CodeMsg codeMsg, Throwable cause) {
        super(codeMsg.getMsg(), cause);
        this.codeMsg=codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
