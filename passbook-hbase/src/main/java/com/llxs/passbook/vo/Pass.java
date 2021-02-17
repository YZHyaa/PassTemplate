package com.llxs.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * HBase 的 Pass（优惠券使用信息） 实体对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pass {

    /** pass 在 HBase 中的 RowKey */
    private String rowKey;

    /** 用户 id */
    private Long userId;

    /** PassTemplate 在 HBase 中的 RowKey */
    private String templateId;

    /** 优惠券 token, 有可能是 null, 则填充 -1 */
    private String token;

    /** 领取日期 */
    private Date assignedDate;

    /** 消费日期, 不为空代表已经被消费了 */
    private Date conDate;
}
