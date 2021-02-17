package com.llxs.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户领取优惠券的请求对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GainPassTemplateRequest {

    /**  用户 ID */
    private Long userId;

    /** PassTemplate 对象 */
    private PassTemplate passTemplate;
}
