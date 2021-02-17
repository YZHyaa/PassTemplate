package com.llxs.passbook.merchants.service;

import com.llxs.passbook.merchants.vo.CreateMerchantsRequest;
import com.llxs.passbook.merchants.vo.PassTemplate;
import com.llxs.passbook.merchants.vo.Response;

public interface MerchantsService {

    /**
     * 创建商户信息
     * */
    Response createMerchants(CreateMerchantsRequest request);

    /**
     * 根据 ID 查询用户信息
     * */
    Response getMerchantsInfo(Integer merchantId);

}
