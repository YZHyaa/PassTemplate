package com.llxs.passbook.passbook.service;

import com.llxs.passbook.passbook.vo.Response;

public interface InventoryPassService {

    /**
     * 获取库存信息
     * 库存 = 全部 PassTemplate - 用户有的
     */
    Response getInventoryInfo();

}
