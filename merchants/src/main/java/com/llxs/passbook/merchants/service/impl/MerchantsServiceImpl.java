package com.llxs.passbook.merchants.service.impl;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.merchants.constant.Constants;
import com.llxs.passbook.merchants.constant.ErrorCode;
import com.llxs.passbook.merchants.dao.MerchantsDao;
import com.llxs.passbook.merchants.entity.Merchants;
import com.llxs.passbook.merchants.service.MerchantsService;
import com.llxs.passbook.merchants.vo.CreateMerchantsRequest;
import com.llxs.passbook.merchants.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 商户服务接口实现
 * */
@Slf4j
@Service
public class MerchantsServiceImpl implements MerchantsService {

    @Autowired
    private MerchantsDao merchantsDao;

    @Autowired
    StringRedisTemplate redisTemplate;


    @Override
    public Response createMerchants(CreateMerchantsRequest request) {
        Response response = new Response();

        ErrorCode errorCode = request.validate(merchantsDao);
        if (errorCode != ErrorCode.SUCCESS) {
            response.setCode(errorCode.getCode());
            response.setMsg(errorCode.getDesc());
        }

        Merchants merchants = request.toMerchants();
        Integer merchantId = merchantsDao.save(merchants).getCid();

        redisTemplate.opsForValue().set(Constants.MERCHANTS_PREFIX + merchantId, JSON.toJSONString(merchants));

        response.setData(merchantId);
        return response;
    }

    @Override
    public Response getMerchantsInfo(Integer merchantId) {

        Response response = new Response();

        Merchants merchants = merchantsDao.findByCid(merchantId);
        if (null == merchants) {
            response.setCode(ErrorCode.MERCHANTS_NOT_EXIST.getCode());
            response.setMsg(ErrorCode.MERCHANTS_NOT_EXIST.getDesc());
        }

        String redisKey = Constants.MERCHANTS_PREFIX + merchantId;
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(merchants));
        response.setData(merchants);

        return response;
    }


}
