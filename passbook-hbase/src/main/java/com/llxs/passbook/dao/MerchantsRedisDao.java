package com.llxs.passbook.dao;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.constant.Constants;
import com.llxs.passbook.entity.Merchants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MerchantsRedisDao {

    @Autowired
    StringRedisTemplate redisTemplate;

    public Merchants findMerchantsById(Integer id) {

        String MerchantsJson = redisTemplate.opsForValue().get(Constants.MERCHANTS_PREFIX + id);
        Merchants merchants = JSON.parseObject(MerchantsJson, Merchants.class);
        return merchants;
    }

    public List<Merchants> findMerchantsByIdIn(List<Integer> ids) {

        List<Merchants> resList = new ArrayList<>();
        for (Integer id : ids) {
            String MerchantsJson = redisTemplate.opsForValue().get(Constants.MERCHANTS_PREFIX + id);
            Merchants merchants = JSON.parseObject(MerchantsJson, Merchants.class);
            resList.add(merchants);
        }

        return resList;
    }
}
