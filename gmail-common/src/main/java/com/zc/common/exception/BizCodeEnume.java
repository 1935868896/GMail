package com.zc.common.exception;

public enum BizCodeEnume {
    UNKONW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCPTION(10001,"系统格式校验失败");

    private int code;
    private String msg;
    BizCodeEnume(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
