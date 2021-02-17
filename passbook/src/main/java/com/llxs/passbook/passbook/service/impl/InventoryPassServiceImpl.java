package com.llxs.passbook.passbook.service.impl;

import com.llxs.passbook.passbook.dao.MerchantsRedisDao;
import com.llxs.passbook.passbook.dao.PassTemplateDao;
import com.llxs.passbook.passbook.entity.PassTemplate;
import com.llxs.passbook.passbook.service.InventoryPassService;
import com.llxs.passbook.passbook.service.UserPassService;
import com.llxs.passbook.passbook.vo.Merchants;
import com.llxs.passbook.passbook.vo.PassTemplateInfo;
import com.llxs.passbook.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryPassServiceImpl implements InventoryPassService {

    @Autowired
    private UserPassService userPassService;

    @Autowired
    private MerchantsRedisDao merchantsDao;

    @Autowired
    private PassTemplateDao passTemplateDao;

    @Override
    public Response getInventoryInfo() {

        Response resp = userPassService.getUserAllPassInfo();
        List<PassTemplateInfo> userAllPassInfos = (List<PassTemplateInfo>) resp.getData();
        List<Long> excludePassTemplateIds = userAllPassInfos.stream().map(
                p -> p.getPassTemplate().getId()
        ).collect(Collectors.toList());


        List<PassTemplate> availablePassTemplates = getAvailablePassTemplate(excludePassTemplateIds);
        List<PassTemplateInfo> passTemplateInfos = buildPassTemplateInfo(availablePassTemplates);

        return new Response(passTemplateInfos);
    }

    /**
     * 用户可见库存 = 所有优惠券（过审） - 未发完 - 有效期内 - 用户领过的（消费过的、未消费过的）
     */
    private List<PassTemplate> getAvailablePassTemplate(List<Long> excludePassTemplateIds) {

        // 审核通过 && 未发完
        List<PassTemplate> validTemplates = passTemplateDao.findByPassIsTrueAndLimitGreaterThan(0);

        ArrayList<PassTemplate> availablePassTemplates = new ArrayList<>();
        for (PassTemplate template : validTemplates) {

            // 过滤用户已领取的
            if (excludePassTemplateIds.contains(template.getId())) {
                continue;
            }

            // 根据有效期过滤优惠券
            if (new Date().getTime() >= template.getStart().getTime()
                    && new Date().getTime() <= template.getEnd().getTime()) {
                availablePassTemplates.add(template);
            }
        }
        return availablePassTemplates;
    }

    private List<PassTemplateInfo> buildPassTemplateInfo(List<PassTemplate> availablePassTemplates) {

        List<Integer> merchantsIds = availablePassTemplates.stream().map(
                t -> t.getMerchantId()
        ).collect(Collectors.toList());

        List<Merchants> merchants = merchantsDao.findMerchantsByIdIn(merchantsIds);

        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        merchants.forEach( m -> merchantsMap.put(m.getCid(), m));

        List<PassTemplateInfo> result = new ArrayList<>(availablePassTemplates.size());
        for (PassTemplate passTemplate : availablePassTemplates) {

            Merchants mc = merchantsMap.getOrDefault(passTemplate.getId(), null);
            if (null == mc) {
                log.error("Merchants Error: {}", passTemplate.getId());
                continue;
            }

            result.add(new PassTemplateInfo(passTemplate, mc));
        }

        return result;

    }
}
