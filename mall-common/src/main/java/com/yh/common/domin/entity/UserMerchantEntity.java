package com.yh.common.domin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;

@Data
@Table(name = "user_merchant")
public class UserMerchantEntity implements Serializable {
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
     * 0-正常 1-禁用
     */
    private Byte status;

    /**
     * 商户名
     */
    @Column(name = "merchant_name")
    private String merchantName;

    /**
     * 商户类型
     */
    @Column(name = "merchant_type")
    private Integer merchantType;

    /**
     * 地址
     */
    private String address;

    /**
     * 开放时间
     */
    @Column(name = "open_time")
    private Date openTime;

    /**
     * 关闭时间
     */
    @Column(name = "close_time")
    private Date closeTime;

    /**
     * 店内环境照片
     */
    private String photos;

    /**
     * 营业资质图片
     */
    @Column(name = "certification_images")
    private String certificationImages;

    /**
     * 补充说明
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}