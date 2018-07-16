package com.zz.map.common;

public enum ResponseCode {
    SUCCESS(0,"OK"),
    ERROR(1,"NOT FOUND"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT"),
    NEEDLOG_IN(10,"NEED_LOGIN");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }

    public String getDesc(){
        return desc;
    }

}
