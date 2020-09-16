package com.zc.common.exception;
/*
* 10:通用
* 11:商品
* 12:订单
* 13:购物车
* 14:物流
* 15:用户
*
*
* */
public enum BizCodeEnume {
    UNKONW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCPTION(10001,"系统格式校验失败"),
    VAILD_SMS_EXCPTION(10002,"短信验证码获取频率太高,稍后再试"),

    PRODUCT_UP_EXCEPTION(11000,"商品上架异常"),
    USER_EXIST_EXCEPTION(15001,"用户存在"),
    PHONE_EXIST_EXCEPTION(15002,"手机号码存在"),
    LOGIN_PASSWORD_INVALD_EXCEPTION(15003,"用户名或密码错误");
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
