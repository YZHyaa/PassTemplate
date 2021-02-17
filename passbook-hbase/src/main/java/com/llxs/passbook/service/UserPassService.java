package com.llxs.passbook.service;

import com.llxs.passbook.vo.Response;

public interface UserPassService {

    /**
     * 获取用户未使用的优惠券
     */
    Response getUserPassInfo(Long userId) throws Exception;

    /**
     * 获取用户已经消费了的优惠券
     */
    Response getUserUsedPassInfo(Long userId) throws Exception;

    /**
     * 获取用户所有的优惠券
     */
    Response getUserAllPassInfo(Long userId) throws Exception;

}
