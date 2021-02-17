package com.llxs.passbook.passbook.vo;

import com.llxs.passbook.passbook.dao.PassTemplateDao;
import com.llxs.passbook.passbook.entity.PassTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassRequest {

    /** 要使用优惠券的商品 ID & 优惠券 ID*/
    private String passRequest;

    @Value("${llxs.yiwei.decrpt}")
    private int decrptKey;

    /**
     * 解密
     */
    public String decrpt() {
        char[] chars = this.passRequest.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] -= decrptKey;
        }
        return String.valueOf(chars);
    }

    /**
     * 获取templateId：
     *      1.解密
     *      2.校验携带的goodsId和对应template的goodsId
     */
    public long getTemplateId(PassTemplateDao passTemplateDao) {

        String decrpt = decrpt();
        String[] split = decrpt.split("&");
        long goodsId = Long.valueOf(split[0]);
        long templateId = Long.valueOf(split[1]);

        // 校验携带的goodsId和对应template的goodsId
        PassTemplate template = passTemplateDao.findById(templateId);
        if(template.getGoodsId() == goodsId) {
            throw new RuntimeException("args error");
        }

        return templateId;
    }
}
