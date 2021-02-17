package com.llxs.passbook.passbook.service.impl;

import com.llxs.passbook.passbook.config.security.AccessContext;
import com.llxs.passbook.passbook.constant.Constants;
import com.llxs.passbook.passbook.dao.PassDao;
import com.llxs.passbook.passbook.dao.PassTemplateDao;
import com.llxs.passbook.passbook.entity.Pass;
import com.llxs.passbook.passbook.entity.PassTemplate;
import com.llxs.passbook.passbook.service.PassOperatorService;
import com.llxs.passbook.passbook.service.UserPassService;
import com.llxs.passbook.passbook.vo.PassReponse;
import com.llxs.passbook.passbook.vo.PassRequest;
import com.llxs.passbook.passbook.vo.PassTemplateInfo;
import com.llxs.passbook.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class PassOperatorServiceImpl implements PassOperatorService {

    @Autowired
    private PassTemplateDao passTemplateDao;

    @Autowired
    private PassDao passDao;

    @Autowired
    private UserPassService userPassService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    /**
     * 1.判断能否领优惠券
     *      优惠券是否有剩余
     *      优惠券是否过期
     * 2.数据记录
     *      PassTemplate 表 limit-1
     *      Pass 表添加一条记录（判断是否有token）
     */
    public Response gainPassTempate(long templateId) {

        PassTemplate passTemplate = passTemplateDao.findById(templateId);

        // 判断优惠券是否有剩余
        if (passTemplate.getLimit() <= 0 && passTemplate.getLimit() != -1) {
            return Response.failure("PassTemplate Limit Max!");
        }
        // 判断优惠券是否过期
        Date cur = new Date();
        if (!(cur.getTime() >= passTemplate.getStart().getTime()
                && cur.getTime() < passTemplate.getEnd().getTime())) {
            return Response.failure("PassTemplate ValidTime Error!");
        }

        // 优惠券limit减一
        passTemplate.setLimit(passTemplate.getLimit() - 1);
        passTemplateDao.saveAndFlush(passTemplate);

        // 将领取记录到 Pass 表
        if(!addPassForUser(passTemplate)) {
            return Response.failure("GainPassTemplate Failure!");
        }

        return Response.success();
    }

    /**
     * 将领取优惠券的信息加入到 Pass 表
     */
    private boolean addPassForUser(PassTemplate passTemplate) {

        // 判断该 PassTemplate 是否有 token
        if (passTemplate.getHasToken()) {
            // 在 redis 中获取该 PassTemplate 的 token（key为PassTemplateId）
            String token = redisTemplate.opsForSet().pop(Constants.TOKEN_PREFIX + passTemplate.getId());
            if (null == token) {
                log.error("Token not exist: {}", passTemplate.getId());
                return false;
            }
            // 将使用的 token 记录到文件
            if (!recordTokenToFile(passTemplate.getMerchantId(), passTemplate.getId(), token)) {
                return false;
            }
        }

        return false;
    }

    /**
     * 将使用过的 token 记录到文件
     */
    private boolean recordTokenToFile(Integer merchantsId, long passTemplateId, String token) {
        try {
            Files.write(
                    // /tmp/token/merchantsId/passTemplateId_
                    Paths.get(Constants.TOKEN_DIR,
                            String.valueOf(merchantsId),
                            passTemplateId  + Constants.USED_TOKEN_SUFFIX),
                    (token + '\n').getBytes(),
                    // 新建、追加
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            log.error("Write token to File error {}", e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Response getGoodsPassTemplate(long goodsId) {

        List<PassTemplateInfo> infos = (List<PassTemplateInfo>) userPassService.getUserPassInfo().getData();
        List<PassTemplateInfo> result = new ArrayList<>();
        // 根据优惠券的 goodId 判断
        infos.forEach(template -> {
            if (template.getPassTemplate().getGoodsId() == goodsId) {
                result.add(template);
            }
        });
        return new Response(result);
    }

    @Override
    /**
     * 1.判断当前用户是否有该优惠券
     * 2.判断优惠券是否属于该商品
     * 3.判断该优惠券是否过期
     * 4.如果优惠券有 token，校验token是否有效
     */
    public Response usePassTemplate(PassRequest request) throws IOException {

        List<Pass> pass = passDao.findByUserIdAndTemplateId(AccessContext.getUserId(), request.getTemplateId(passTemplateDao));
        if (pass.size() != 1) {
            return Response.failure("User doesn't have this PassTemplate");
        }

        PassTemplate passTemplate = passTemplateDao.findById(request.getTemplateId(passTemplateDao));

        if (!(new Date().getTime() >= passTemplate.getStart().getTime()
                && new Date().getTime() <= passTemplate.getEnd().getTime())) {
            return Response.failure("PassTemplate timed Error");
        }

        if(passTemplate.getHasToken()) {
            String token = pass.get(0).getToken();
            if (StringUtils.isBlank(token)) {
                return Response.failure("PassTemplate Don't has token");
            }

            int merchantId = passTemplate.getMerchantId();
            Path path = Paths.get(Constants.TOKEN_DIR,String.valueOf(merchantId),
                                 String.valueOf(passTemplate.getId()) + Constants.USED_TOKEN_SUFFIX);

            Stream<String> lines = Files.lines(path);
            Set<String> userdTokens = lines.collect(Collectors.toSet());
            if (!userdTokens.contains(token)) {
                return Response.failure("token is invalid");
            }
        }

        // 使用优惠券，即将相应记录的过期时间赋值
        Pass p = pass.get(0);
        passDao.saveAndFlush(p);

        // 返回具体折扣
        String passReponse = new PassReponse().encrpt(passTemplate.getGoodsId(), passTemplate.getDiscount());
        return new Response(passReponse);
    }
}
