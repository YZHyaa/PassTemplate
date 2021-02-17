package com.llxs.passbook.passbook.dao;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.passbook.constant.Constants;
import com.llxs.passbook.passbook.vo.Merchants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MerchantsRedisDao {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${llxs.merchant.url}")
    private String merchantUrl;

    public List<Merchants> findMerchantsByIdIn(List<Integer> merchatnIds) {

        List<Merchants> resList = new ArrayList<>();
        for (Integer id : merchatnIds) {
            resList.add(findMerchantsById(id));
        }

        return resList;
    }

    public Merchants findMerchantsById(Integer merchantId) {

        String MerchantsJson = redisTemplate.opsForValue().get(Constants.MERCHANTS_PREFIX + merchantId);

        if (StringUtils.isBlank(MerchantsJson)) {
            MerchantsJson = requestMerchantInfo(merchantId);
            if (MerchantsJson == null) return null;
        }

        Merchants merchants = JSON.parseObject(MerchantsJson, Merchants.class);
        return merchants;
    }

    /**
     * 发起远程调用，并将结果结果放到 redis
     */
    private String requestMerchantInfo(Integer merchantId) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = merchantUrl + "/" + merchantId;
        HttpGet getReq = new HttpGet(url);

        String MerchantsJson = "";
        try {
            // 发起 HttpClient 远程调用
            HttpResponse remoteResp = httpClient.execute(getReq);
            if (remoteResp.getStatusLine().getStatusCode() == 200) {
                MerchantsJson = EntityUtils.toString(remoteResp.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取商户信息失败 {}", merchantId);
            return null;
        }

        redisTemplate.opsForValue().set(Constants.MERCHANTS_PREFIX + merchantId, MerchantsJson);
        return MerchantsJson;
    }
}