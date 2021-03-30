package com.yh.common.domin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "user_resource_category")
public class UserResourceCategoryEntity implements Serializable {
    @Id
    private Long id;

    private Byte deleted;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "cate_key")
    private Long cateKey;
    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    private static final long serialVersionUID = 1L;
}