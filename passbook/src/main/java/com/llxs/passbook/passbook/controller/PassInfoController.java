package com.llxs.passbook.passbook.controller;

import com.llxs.passbook.passbook.config.security.AccessContext;
import com.llxs.passbook.passbook.log.LogConstants;
import com.llxs.passbook.passbook.log.LogGenerator;
import com.llxs.passbook.passbook.service.InventoryPassService;
import com.llxs.passbook.passbook.service.UserPassService;
import com.llxs.passbook.passbook.vo.PassTemplateInfo;
import com.llxs.passbook.passbook.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/passbook")
public class PassInfoController {

    @Autowired
    /** 获取用户优惠券服务 */
    private UserPassService userPassService;

    @Autowired
    /** 获取库存优惠券服务 */
    private InventoryPassService inventoryService;

    @Resource
    private HttpServletRequest httpServletRequest;

    /**
     * 获取库存信息（用户未拥有的优惠券）
     * @return List<PassTemplateInfo>{@link PassTemplateInfo}
     */
    @GetMapping("/inventoryinfo")
    Response getInventoryInfo() {

        LogGenerator.genLog(
                httpServletRequest,
                AccessContext.getUserId(),
                LogConstants.ActionName.INVENTORY_INFO,
                null
        );

        return inventoryService.getInventoryInfo();
    }

    /**
     * 获取用户已有，但未使用的优惠券信息，即我的优惠券功能实现
     * @return List<PassTemplateInfo> {@link PassTemplateInfo}
     */
    @GetMapping("/userpassinfo")
    Response getUserPassInfo() throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                AccessContext.getUserId(),
                LogConstants.ActionName.USER_PASS_INFO,
                null
        );

        return userPassService.getUserPassInfo();
    }


    /**
     * 获取用户使用了的优惠券信息, 即已使用优惠券功能实现
     * @return List<PassTemplateInfo> {@link PassTemplateInfo}
     */
    @GetMapping("useruserdpassinfo")
    Response getUserUsedPassInfo() throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                AccessContext.getUserId(),
                LogConstants.ActionName.USER_USED_PASS_INFO,
                null
        );

        return userPassService.getUserUsedPassInfo();
    }

}
