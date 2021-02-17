package com.llxs.passbook.merchants.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@ApiModel("商户实体对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "merchants")
public class Merchants {


    @ApiModelProperty("商户 id, 主键")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer cid;

    @ApiModelProperty("商户名称, 需要是全局唯一的")
    @Basic
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ApiModelProperty("商户 logo")
    @Basic
    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    @ApiModelProperty("商户营业执照")
    @Basic
    @Column(name = "business_license_url", nullable = false)
    private String businessLicenseUrl;

    @ApiModelProperty("商户的联系电话")
    @Basic
    @Column(name = "phone", nullable = false)
    private String phone;

    @ApiModelProperty("商户地址")
    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @ApiModelProperty("商户是否通过审核")
    @Basic
    @Column(name = "is_audit", nullable = false)
    private Boolean isAudit = false;
}
