package com.yh.common.domin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author yh
 */
@Data
@Table(name = "user_menu")
public class UserMenuEntity implements Serializable {
    @Id
    private Long id;

    private Byte deleted;

    /**
     * 父级ID
     */
    @Column(name = "parent_key")
    private Long parentKey;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "menu_key")
    private Long menuKey;
    /**
     * 菜单名称
     */
    private String title;

    /**
     * 菜单级数
     */
    private Integer level;

    /**
     * 菜单排序
     */
    private Integer sort;

    /**
     * 前端名称
     */
    private String name;

    /**
     * 前端图标
     */
    private String icon;

    /**
     * 前端隐藏
     */
    private Integer hidden;

    private static final long serialVersionUID = 1L;
}