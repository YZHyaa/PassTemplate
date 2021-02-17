package com.llxs.passbook.controller;

import com.llxs.passbook.log.LogConstants;
import com.llxs.passbook.log.LogGenerator;
import com.llxs.passbook.service.InventoryPassService;
import com.llxs.passbook.service.OperatorPassService;
import com.llxs.passbook.service.UserPassService;
import com.llxs.passbook.vo.GainPassTemplateRequest;
import com.llxs.passbook.vo.Pass;
import com.llxs.passbook.vo.PassInfo;
import com.llxs.passbook.vo.InventoryResponse;
import com.llxs.passbook.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/passbook")
public class PassbookController {

    @Autowired
    /** 获取用户优惠券服务 */
    private UserPassService userPassService;

    @Autowired
    /** 获取库存优惠券服务 */
    private InventoryPassService inventoryService;

    @Autowired
    /** 优惠券领取、使用等服务 */
    private OperatorPassService operatorPassService;

    @Autowired
    /** HttpServletRequest */
    private HttpServletRequest httpServletRequest;

    /**
     * 获取库存信息（用户未拥有的优惠券）
     * @param userId
     * @return InventoryResponse {@link InventoryResponse}
     */
    @GetMapping("/inventoryinfo")
    Response getInventoryInfo(Long userId) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.INVENTORY_INFO,
                null
        );

        return inventoryService.getInventoryInfo(userId);
    }

    /**
     * 用户领取优惠券
     * @param request {@link GainPassTemplateRequest}
     * @return true/false
     */
    @PostMapping("/gainpasstemplate")
    Response gainpasstemplate(@RequestBody GainPassTemplateRequest request) {

        LogGenerator.genLog(
                httpServletRequest,
                request.getUserId(),
                LogConstants.ActionName.GAIN_PASS_TEMPLATE,
                request
        );

        return  operatorPassService.gainPassTempate(request);
    }


    /**
     * 获取用户已有，但未使用的优惠券信息，即我的优惠券功能实现
     * @param userId
     * @return List<PassInfo> {@link PassInfo}
     */
    @GetMapping("/userpassinfo")
    Response getUserPassInfo(Long userId) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.USER_PASS_INFO,
                null
        );

        return userPassService.getUserPassInfo(userId);
    }

    /**
     * 用户使用优惠券
     * @param pass {@link Pass}
     * @return true/false
     */
    @PostMapping("/userusepass")
    Response usePassTemplate(@RequestBody Pass pass) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                pass.getUserId(),
                LogConstants.ActionName.USER_USE_PASS,
                pass
        );

        return operatorPassService.usePassTemplate(pass);
    }

    /**
     * 获取用户使用了的优惠券信息, 即已使用优惠券功能实现
     * @param userId
     * @return List<PassInfo> {@link PassInfo}
     */
    @GetMapping("useruserdpassinfo")
    Response getUserUsedPassInfo(Long userId) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.USER_USED_PASS_INFO,
                null
        );

        return userPassService.getUserUsedPassInfo(userId);
    }

}
