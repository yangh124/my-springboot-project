package com.yh.common.domin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;

@Data
@Table(name = "user_resource")
public class UserResourceEntity implements Serializable {
    @Id
    private Long id;

    private Byte deleted;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "resource_key")
    private Long resourceKey;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源URL
     */
    private String url;

    /**
     * 描述
     */
    private String description;

    /**
     * 资源分类ID
     */
    @Column(name = "category_key")
    private Long categoryKey;

    private static final long serialVersionUID = 1L;
}