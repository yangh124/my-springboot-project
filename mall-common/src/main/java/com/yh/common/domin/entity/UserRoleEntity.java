package com.yh.common.domin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;

@Data
@Table(name = "user_role")
public class UserRoleEntity implements Serializable {
    @Id
    private Long id;

    private Byte deleted;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    @Column(name = "role_key")
    private Long roleKey;

    /**
     * 后台用户数量
     */
    @Column(name = "admin_count")
    private Integer adminCount;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 启用状态：0->禁用；1->启用
     */
    private Integer status;

    private Integer sort;

    private static final long serialVersionUID = 1L;
}