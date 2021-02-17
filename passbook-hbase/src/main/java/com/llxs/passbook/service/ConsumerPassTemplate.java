package com.llxs.passbook.service;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.constant.Constants;
import com.llxs.passbook.vo.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 消费 Kafka 中的 PassTemplate
 */
@Slf4j
@Component
public class ConsumerPassTemplate {

    @Autowired
    private HBasePassService passService;


    @KafkaListener(topics = {Constants.TEMPLATE_TOPIC})
    public void receive(@Payload String passTemplate,
                        @Header(KafkaHeaders.MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.PARTITION_ID) int partition,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){

        log.info("Consumer Receive PassTemplate: {}", passTemplate);
        PassTemplate pt = JSON.parseObject(passTemplate, PassTemplate.class);

        log.info("DropPassTemplateToHBase: {}", passService.dropPassTemplateToHBase(pt));
    }
}
