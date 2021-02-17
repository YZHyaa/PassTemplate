package com.llxs.passbook.passbook.controller;

import com.llxs.passbook.passbook.config.security.AccessContext;
import com.llxs.passbook.passbook.log.LogConstants;
import com.llxs.passbook.passbook.log.LogGenerator;
import com.llxs.passbook.passbook.service.PassOperatorService;
import com.llxs.passbook.passbook.vo.PassRequest;
import com.llxs.passbook.passbook.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/passbook")
public class PassOperatorController {

    @Autowired
    /** 优惠券领取、使用等服务 */
    private PassOperatorService passOperatorService;

    @Resource
    private HttpServletRequest httpServletRequest;

    /**
     * 用户领取优惠券
     * @param templateId 优惠券ID
     * @return true/false
     */
    @PutMapping("/gainpasstemplate/{templateId}")
    Response gainpasstemplate(@PathVariable("templateId") long templateId) {

        LogGenerator.genLog(
                httpServletRequest,
                AccessContext.getUserId(),
                LogConstants.ActionName.GAIN_PASS_TEMPLATE,
                templateId
        );

        return passOperatorService.gainPassTempate(templateId);
    }

    /**
     * 获取用户对应商品的可用优惠券
     * @param goodsId
     * @return
     */
    @GetMapping("/goodspassinfo/{goodsId}")
    Response getGoodsPassTemplate(@PathVariable("goodsId") long goodsId) {
        return passOperatorService.getGoodsPassTemplate(goodsId);
    }

    /**
     * 用户使用优惠券
     * @param request {@link PassRequest}
     * @return {@link com.llxs.passbook.passbook.vo.PassReponse}
     */
    @PostMapping("/usepasstemplate/{templateId}")
    Response usePassTemplate(@RequestBody PassRequest request) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                AccessContext.getUserId(),
                LogConstants.ActionName.USER_USE_PASS,
                request
        );

        return passOperatorService.usePassTemplate(request);
    }
}
