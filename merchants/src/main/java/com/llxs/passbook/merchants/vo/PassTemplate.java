package com.llxs.passbook.merchants.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.llxs.passbook.merchants.constant.ErrorCode;
import com.llxs.passbook.merchants.dao.MerchantsDao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel("投放优惠券的请求对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassTemplate {

    @ApiModelProperty("优惠券ID")
    private long id;

    @ApiModelProperty("所属商户id")
    private Integer merchantId;

    @ApiModelProperty("打折商品 id ")
    private long goodsId;

    @ApiModelProperty("折扣值（80=八折，75=七五折）")
    private int discount;

    @ApiModelProperty("优惠券标题")
    private String title;

    @ApiModelProperty("优惠券摘要")
    private String summary;

    @ApiModelProperty("优惠券的详细信息")
    private String desc;

    @ApiModelProperty("最大个数限制 ")
    private Long limit;

    @ApiModelProperty("优惠券是否有 Token, 用于商户核销")
    private Boolean hasToken; // token 存储于 Redis Set 中, 每次领取从 Redis 中获取

    @ApiModelProperty("优惠券背景色")
    private Integer background;

    @ApiModelProperty("优惠券开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start;

    @ApiModelProperty("优惠券结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;

    /**
     * 校验优惠券参数的合法性
     * @param merchantsDao {@link MerchantsDao}
     * @return {@link ErrorCode}
     */
    public ErrorCode validate(MerchantsDao merchantsDao) {

        if (null == merchantsDao.findByCid(merchantId)) {
            return ErrorCode.MERCHANTS_NOT_EXIST;
        }

        return ErrorCode.SUCCESS;
    }
}
