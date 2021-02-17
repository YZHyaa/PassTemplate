package com.llxs.passbook.service;

import com.llxs.passbook.vo.GainPassTemplateRequest;
import com.llxs.passbook.vo.Pass;
import com.llxs.passbook.vo.Response;

import java.io.IOException;

public interface OperatorPassService {

    /**
     * 获取优惠券
     */
    Response gainPassTempate(GainPassTemplateRequest request);

    /**
     * 使用优惠券
     */
    Response usePassTemplate(Pass pass) throws IOException, Exception;
}
