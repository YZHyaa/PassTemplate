package com.llxs.passbook.merchants.controller;

import com.llxs.passbook.merchants.service.PassTemplateService;
import com.llxs.passbook.merchants.vo.PassTemplate;
import com.llxs.passbook.merchants.vo.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/merchants")
public class PassTemplateController  {

    @Autowired
    private PassTemplateService passTemplateService;

    @ApiOperation("投放优惠券")
    @PostMapping("/passtemplate")
    public Response dropPassTemplate(@RequestBody PassTemplate passTemplate) {

        log.info("DropPassTemplate: {}", passTemplate);
        return passTemplateService.dropPassTemplate(passTemplate);
    }

    @ApiOperation("查看商户已投放优惠券")
    @GetMapping("/passtemplate/{id}")
    public Response queryPassTemplates(@PathVariable("id") Integer merchantId) {
        return passTemplateService.queryPassTemplateList(merchantId);
    }

    @ApiOperation("修改已投放优惠券信息")
    @PutMapping("/passtemplate")
    public Response updatePassTemplate(@RequestBody PassTemplate passTemplate) {

        // 注：此时 PassTemplate 是有 id 的，所以是修改
        Response response = passTemplateService.updatePassTemplate(passTemplate);

        return response;
    }




}
