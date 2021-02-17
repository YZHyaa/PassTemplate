package com.llxs.passbook.service;

import com.llxs.passbook.vo.Response;

public interface InventoryPassService {

    /**
     * 获取库存信息
     * 库存 = 全部 PassTemplate - 用户有的
     */
    Response getInventoryInfo(Long userId) throws Exception;
}
