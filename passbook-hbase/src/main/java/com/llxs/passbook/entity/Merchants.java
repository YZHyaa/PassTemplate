package com.llxs.passbook.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel("商户实体对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchants {


    @ApiModelProperty("商户 id, 主键")
    private Integer cid;

    @ApiModelProperty("商户名称, 需要是全局唯一的")
    private String name;

    @ApiModelProperty("商户 logo")
    private String logoUrl;

    @ApiModelProperty("商户营业执照")
    private String businessLicenseUrl;

    @ApiModelProperty("商户的联系电话")
    private String phone;

    @ApiModelProperty("商户地址")
    private String address;

    @ApiModelProperty("商户是否通过审核")
    private Boolean isAudit = false;
}

