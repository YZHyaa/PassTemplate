package com.llxs.passbook.passbook.service.impl;

import com.llxs.passbook.passbook.config.security.AccessContext;
import com.llxs.passbook.passbook.constant.PassStatus;
import com.llxs.passbook.passbook.dao.MerchantsRedisDao;
import com.llxs.passbook.passbook.dao.PassDao;
import com.llxs.passbook.passbook.dao.PassTemplateDao;
import com.llxs.passbook.passbook.entity.Pass;
import com.llxs.passbook.passbook.entity.PassTemplate;
import com.llxs.passbook.passbook.service.UserPassService;
import com.llxs.passbook.passbook.vo.Merchants;
import com.llxs.passbook.passbook.vo.PassTemplateInfo;
import com.llxs.passbook.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserPassServiceImpl implements UserPassService {

    @Autowired
    private PassTemplateDao passTemplateDao;

    @Autowired
    private PassDao passDao;

    @Autowired
    private MerchantsRedisDao merchantsDao;

    @Override
    public Response getUserPassInfo() {
        return getPassInfoByStatus( PassStatus.UNUSED);
    }

    @Override
    public Response getUserUsedPassInfo() {
        return getPassInfoByStatus( PassStatus.USED);
    }

    @Override
    public Response getUserAllPassInfo() {
        return getPassInfoByStatus( PassStatus.ALL);
    }

    /**
     * 根据优惠券状态获取优惠券信息
     * 1.构建查询条件
     *      1.用户全部优惠券：userId
     *      2.用户未使用：userId && （未使用 || 未过期）
     *      3.用户已使用：userId && （已使用 || 已过期）
     * 2.组装返回结果
     *      1.pass -> passTemplate
     *      2.passTemplate -> Merchant
     */
    private Response getPassInfoByStatus(PassStatus status){

        // 获取当前用户的优惠券记录
        List<Pass> passInfos = getPassesByStatus(status);
        // 根据 Pass 获取所有 PassTemplate
        Map<Long, PassTemplate> passTemplateMap = buildPassTemplateMap(passInfos);
        // 根据 PassTemplate 获取所有商户 Merchant
        Map<Integer, Merchants> merchantsMap = buildMerchantsMap(new ArrayList<>(passTemplateMap.values()));
        // 构建返回结果
        List<PassTemplateInfo> passTemplateInfos = buildPassTemplateInfo(passInfos, passTemplateMap, merchantsMap);
        return new Response(passTemplateInfos);
    }

    private List<Pass> getPassesByStatus(PassStatus status) {
        // 获取当前用户的优惠券使用信息
        List<Pass> passInfos;
        if (status ==  PassStatus.ALL) {
            passInfos = passDao.findByUserId(AccessContext.getUserId());
        } else {
            passInfos = passDao.findByUserIdAndConDate(AccessContext.getUserId(), null);
        }

        return passInfos;
    }

    private Map<Long, PassTemplate> buildPassTemplateMap(List<Pass> passInfos) {

        List<Long> templateIds = passInfos.stream().map(
                p -> p.getTemplateId()
        ).collect(Collectors.toList());

        List<PassTemplate> templateList = passTemplateDao.findByIdIn(templateIds);

        // <id, PassTemplate>
        Map<Long, PassTemplate> passTemplateMap = new HashMap<>();
        for (PassTemplate passTemplate : templateList) {
            passTemplateMap.put(passTemplate.getId(), passTemplate);
        }

        return passTemplateMap;
    }

    private Map<Integer, Merchants> buildMerchantsMap(List<PassTemplate> passTemplateList) {

        List<Integer> merchantIds = passTemplateList.stream().map(
                PassTemplate::getMerchantId
        ).collect(Collectors.toList());

        List<Merchants> merchantsList = merchantsDao.findMerchantsByIdIn(merchantIds);

        // <id, Merchants>
        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        merchantsList.forEach(m -> merchantsMap.put(m.getCid(), m));

        return merchantsMap;
    }

    private List<PassTemplateInfo> buildPassTemplateInfo(List<Pass> passInfos,
                                                         Map<Long, PassTemplate> passTemplateMap,
                                                         Map<Integer, Merchants> merchantsMap) {
        List<PassTemplateInfo> result = new ArrayList<>();
        for (Pass pass : passInfos) {
            PassTemplate passTemplate = passTemplateMap.getOrDefault(pass.getTemplateId(), null);
            if (null == passTemplate) {
                log.error("PassTemplate Null : {}", pass.getTemplateId());
                continue;
            }

            Merchants merchant = merchantsMap.getOrDefault(passTemplate.getMerchantId(), null);
            if (null == merchant) {
                log.error("Merchant Null : {}", passTemplate.getId());
                continue;
            }

            result.add(new PassTemplateInfo(passTemplate, merchant));
        }
        return result;
    }

}
