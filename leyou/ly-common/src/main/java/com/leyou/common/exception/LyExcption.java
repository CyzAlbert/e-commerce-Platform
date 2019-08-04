package com.leyou.common.exception;

public class LyExcption extends RuntimeException {
    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    private int errCode;

    public LyExcption(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getMsg());
        this.errCode=exceptionEnum.getCode();
    }
}
