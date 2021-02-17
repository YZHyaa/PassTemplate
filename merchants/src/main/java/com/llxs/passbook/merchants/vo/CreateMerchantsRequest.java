package com.llxs.passbook.merchants.vo;

import com.llxs.passbook.merchants.constant.ErrorCode;
import com.llxs.passbook.merchants.dao.MerchantsDao;
import com.llxs.passbook.merchants.entity.Merchants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("创建商户的请求对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMerchantsRequest {

    @ApiModelProperty("商户名")
    private String name;

    @ApiModelProperty("商户logo的URL")
    private String logoUrl;

    @ApiModelProperty("营业执照URL")
    private String businessLicenseUrl;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("地址")
    private String address;

    /**
     * 验证请求参数的合法性
     * @param merchantsDao {@link MerchantsDao}
     * @return {@link ErrorCode}
     */
    public ErrorCode validate(MerchantsDao merchantsDao) {

        if (merchantsDao.findByName(this.name) != null) {
            return ErrorCode.DUPLICATE_NAME;
        }

        if (null == this.logoUrl) {
            return ErrorCode.EMPTY_LOGO;
        }

        if (null == this.businessLicenseUrl) {
            return ErrorCode.EMPTY_BUSINESS_LICENSE;
        }

        if (null == this.address) {
            return ErrorCode.EMPTY_ADDRESS;
        }

        if (null == this.phone) {
            return ErrorCode.ERROR_PHONE;
        }

        return ErrorCode.SUCCESS;
    }

    /**
     * 将请求对象转换为商户对象
     * @return {@link Merchants}
     */
    public Merchants toMerchants() {

        Merchants merchants = new Merchants();

        merchants.setName(this.name);
        merchants.setLogoUrl(this.logoUrl);
        merchants.setBusinessLicenseUrl(this.businessLicenseUrl);
        merchants.setPhone(phone);
        merchants.setAddress(address);

        return merchants;
    }
}
