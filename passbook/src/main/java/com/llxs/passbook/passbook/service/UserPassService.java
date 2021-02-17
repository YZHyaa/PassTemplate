package com.llxs.passbook.passbook.service;

import com.llxs.passbook.passbook.vo.Response;

public interface UserPassService {

    /**
     * 获取用户未使用的优惠券
     */
    Response getUserPassInfo();

    /**
     * 获取用户已经消费了的优惠券
     */
    Response getUserUsedPassInfo();

    /**
     * 获取用户所有的优惠券
     */
    Response getUserAllPassInfo();
}
