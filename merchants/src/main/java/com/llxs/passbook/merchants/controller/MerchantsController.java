package com.llxs.passbook.merchants.controller;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.merchants.entity.Merchants;
import com.llxs.passbook.merchants.service.MerchantsService;
import com.llxs.passbook.merchants.vo.CreateMerchantsRequest;
import com.llxs.passbook.merchants.vo.Response;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "商户相关接口")
@Slf4j
@RestController
@RequestMapping("/merchants")
public class MerchantsController {

    @Autowired
    private MerchantsService merchantsService;

    @ApiOperation(value = "创建商户", notes = "开发中")
    @ApiResponses({
            @ApiResponse(code = 1, message = "非HTTP状态码，Response code字段值，描述：成功，返回该商户ID"),
            @ApiResponse(code = 0, message = "非HTTP状态码，Response code字段值，描述：失败")
    })
    @PostMapping("/create")
    public Response createMerchants(@RequestBody CreateMerchantsRequest request) {

        log.info("CreateMerchants: {}", JSON.toJSONString(request));
        return merchantsService.createMerchants(request);
    }

    @ApiOperation("获取商户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商户ID")
    })
    @ApiResponses({
            @ApiResponse(code = 1, message = "非HTTP状态码，Response code字段值，描述：成功", response = Merchants.class),
            @ApiResponse(code = 0, message = "非HTTP状态码，Response code字段值，描述：失败")
    })
    @GetMapping("/{id}")
    public Response getMerchantsInfo(@PathVariable Integer id) {

        log.info("GetMerchantsInfo：{}", id);
        return merchantsService.getMerchantsInfo(id);
    }

}
