package com.llxs.passbook.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassReponse {

    /** 要使用优惠券的商品 ID & 折扣*/
    private String passResponse;

    @Value("${llxs.yiwei.encrpt}")
    private int encrptKey;

    /**
     * 对返回结果加密
     */
    public String encrpt(long goodsId, int discount) {

        passResponse = String.valueOf(goodsId) + "&" + String.valueOf(discount);
        char[] chars = passResponse.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] += encrptKey;
        }

        return passResponse;
    }
}
