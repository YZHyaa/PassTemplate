package com.llxs.passbook.merchants.service.impl;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.merchants.constant.Constants;
import com.llxs.passbook.merchants.constant.ErrorCode;
import com.llxs.passbook.merchants.dao.MerchantsDao;
import com.llxs.passbook.merchants.service.PassTemplateService;
import com.llxs.passbook.merchants.vo.PassTemplate;
import com.llxs.passbook.merchants.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@Service
public class PassTemplateServiceImpl implements PassTemplateService {

    @Autowired
    private MerchantsDao merchantsDao;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${passbook.url}")
    private String passbookUrl;

    @Override
    public Response dropPassTemplate(PassTemplate template) {

        Response response = new Response();
        ErrorCode errorCode = template.validate(merchantsDao);

        if (errorCode != ErrorCode.SUCCESS) {
            response.setCode(errorCode.getCode());
            response.setMsg(errorCode.getDesc());
        } else {
            String passTemplate = JSON.toJSONString(template);
            kafkaTemplate.send(
                    Constants.TEMPLATE_TOPIC,
                    Constants.TEMPLATE_TOPIC,
                    passTemplate
            );
            log.info("DropPassTemplates: {}", passTemplate);
        }
        return response;
    }

    @Override
    public Response queryPassTemplateList(Integer merchantId) {

        HttpClient httpClient = HttpClients.createDefault();
        String url = passbookUrl + "/" + merchantId;
        HttpGet getReq = new HttpGet(url);

        String json = "";
        try {
            // 发起 HttpClient 远程调用
            HttpResponse remoteResp = httpClient.execute(getReq);
            if (remoteResp.getStatusLine().getStatusCode() == 200) {
                json = EntityUtils.toString(remoteResp.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取优惠券信息失败 {}", merchantId);
            return Response.failure("获取优惠券信息失败");
        }

        return JSON.parseObject(json, Response.class);
    }

    @Override
    public Response updatePassTemplate(PassTemplate template) {

        String passTemplate = JSON.toJSONString(template);
        kafkaTemplate.send(
                Constants.TEMPLATE_TOPIC,
                Constants.TEMPLATE_TOPIC,
                passTemplate
        );
        log.info("UpdatePassTemplates: {}", passTemplate);

        return Response.success();
    }
}
