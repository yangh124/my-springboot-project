package com.yh.common.domin.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "user_admin_role_relation")
public class UserAdminRoleRelationEntity implements Serializable {
    @Id
    private Long id;

    private Boolean deleted;

    /**
     * 后台用户key
     */
    @Column(name = "user_key")
    private Long userKey;

    /**
     * 角色key
     */
    @Column(name = "role_key")
    private Long roleKey;

    private static final long serialVersionUID = 1L;
}