package com.llxs.passbook.service;

import com.llxs.passbook.vo.PassTemplate;

public interface HBasePassService {

    /** 将 PassTemplate 写入 HBase */
    boolean dropPassTemplateToHBase(PassTemplate pt);
}
