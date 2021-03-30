package com.yh.common.domin.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;

@Data
@Table(name = "user_info")
public class UserInfoEntity implements Serializable {
    /**
     * ID
     */
    @Id
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 0可用;1.不可用
     */
    private Integer deleted;

    /**
     * 用户KEY
     */
    @Column(name = "user_key")
    private Long userKey;

    /**
     * token
     */
    private String token;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 性别(0.无;1.男;2.女)
     */
    private Byte sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 金币
     */
    private Long gold;

    /**
     * 简介
     */
    private String remake;

    /**
     * 经验值
     */
    private BigDecimal experience;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 0-正常 1-禁用
     */
    private Byte status;

    private static final long serialVersionUID = 1L;
}