package com.llxs.passbook.passbook.service;

import com.llxs.passbook.passbook.vo.Response;

public interface MerchantsPassService {

    /**
     * 根据商户Id，返回对应商户的优惠券（远程调用）
     */
    Response getMerchantPassInfo();
}
