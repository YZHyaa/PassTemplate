package com.llxs.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取库存的返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    /** 用户 id */
    private Long userId;

    /** 优惠券模板信息 */
    private List<PassTemplateInfo> passTemplateInfos;
}