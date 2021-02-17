package com.llxs.passbook.vo;

import com.llxs.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户获取优惠券信息的返回对象（未使用、已使用）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassInfo {

    /** 优惠券使用信息 */
    private Pass pass;

    /** 优惠券模板 */
    private PassTemplate passTemplate;

    /** 优惠券对应的商户 */
    private Merchants merchants;
}
