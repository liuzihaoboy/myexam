package com.learning.exam.framework.exception;

import com.learning.exam.model.result.CodeMsg;

/**
 * @author liuzihao
 * @date 2019-01-31  11:22
 */
public class ValidationJsonException extends RuntimeException {
    private CodeMsg codeMsg;

    public ValidationJsonException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.codeMsg=codeMsg;
    }

    public ValidationJsonException(CodeMsg codeMsg, Throwable cause) {
        super(codeMsg.getMsg(), cause);
        this.codeMsg=codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
