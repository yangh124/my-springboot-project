package com.yh.common.domin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author yh
 */
@Data
@Table(name = "user_admin")
public class UserAdminEntity implements Serializable {
    /**
     * 主键ID
     */
    @Id
    private Integer id;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 是否删除:0否;1是
     */
    private Byte deleted;

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
     * 分布式唯一主键
     */
    @Column(name = "user_key")
    private Long userKey;

    /**
     * 用户昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    private String password;

    /**
     * 用户账号
     */
    @Column(name = "user_account")
    private String userAccount;

    private String token;

    private static final long serialVersionUID = 1L;
}