package com.yh.common.enums.exception;


public class BusinessException extends RuntimeException {

    private final int errCode;

    private final String errMsg;

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public BusinessException(ResponseCodeEnum err) {
        super(err.getMessage());
        this.errCode = err.getCode();
        this.errMsg = err.getMessage();
    }

    public BusinessException(int errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BusinessException(int errCode, String errMsg, Throwable throwable) {
        super(errMsg, throwable);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

}
