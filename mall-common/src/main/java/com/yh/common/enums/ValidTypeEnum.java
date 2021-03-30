package com.yh.common.enums;

public enum ValidTypeEnum {
    VALID(0, "有效"),
    UN_VALID(1,"无效"),
    ;

    private final int code;

    private final String des;

    ValidTypeEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
