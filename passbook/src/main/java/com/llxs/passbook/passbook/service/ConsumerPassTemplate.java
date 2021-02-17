package com.llxs.passbook.passbook.service;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.passbook.constant.Constants;
import com.llxs.passbook.passbook.dao.PassTemplateDao;
import com.llxs.passbook.passbook.entity.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ConsumerPassTemplate {

    @Autowired
    private PassTemplateDao passTemplateDao;

    @KafkaListener(topics = {Constants.TEMPLATE_TOPIC})
    public void receive(@Payload String passTemplate) {

        log.info("Consumer Receive PassTemplate: {}", passTemplate);

        PassTemplate pt = JSON.parseObject(passTemplate, PassTemplate.class);

        log.info("DropPassTemplateToHBase: {}", passTemplateDao.save(pt));
    }


}
