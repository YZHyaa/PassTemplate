package com.llxs.passbook.merchants.service;

import com.llxs.passbook.merchants.vo.PassTemplate;
import com.llxs.passbook.merchants.vo.Response;

import java.util.List;

public interface PassTemplateService {
    Response dropPassTemplate(PassTemplate passTemplate);

    Response queryPassTemplateList(Integer id);

    Response updatePassTemplate(PassTemplate passTemplate);
}
