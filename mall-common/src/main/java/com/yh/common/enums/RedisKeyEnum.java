package com.yh.common.enums;


/**
 * 也可以放配置文件
 */
public enum RedisKeyEnum {
    //hash
    GOS_USER_ADMIN("gos:user:admin:"),
    //string
    S_REPEAT_SUBMIT("gos:repeat:submit"),
    //list
    GOS_USER_RESOURCE("gos:user:resourceList"),
    //zset

    ;

    private final String name;

    public String getName() {
        return name;
    }

    RedisKeyEnum(String name) {
        this.name = name;
    }
}
