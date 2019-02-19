package com.learning.exam.framework.exception;

import com.learning.exam.model.result.CodeMsg;

/**
 * @author liuzihao
 * @date 2019-02-13  20:10
 */
public class ValidationHtmlException extends RuntimeException {
    private CodeMsg codeMsg;

    public ValidationHtmlException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.codeMsg=codeMsg;
    }
    public ValidationHtmlException(String codeMsg) {
        super(codeMsg);
    }
    public ValidationHtmlException(CodeMsg codeMsg, Throwable cause) {
        super(codeMsg.getMsg(), cause);
        this.codeMsg=codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
