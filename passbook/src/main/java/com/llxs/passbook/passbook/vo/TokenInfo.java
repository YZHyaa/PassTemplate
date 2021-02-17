package com.llxs.passbook.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT 中携带的信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    /** 请求主体：1.User 2.Merchants */
    private int type;
    /** UserID 或 MerchantsId*/
    private long id;
}
