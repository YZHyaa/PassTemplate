package com.llxs.passbook.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /** 状态码: 正确返回 0，错误返回 -1 */
    private Integer code = 0;

    /** 错误信息，正确返回空字符串 */
    private String msg = "";

    /** 返回值对象 */
    private Object data;

    /**
     * 正确的响应构造函数
     */
    public Response(Object data) {
        this.data = data;
    }

    /**
     * 空响应
     */
    public static Response success() {
        return new Response();
    }

    /**
     * 错误响应
     */
    public static Response failure(String errorMsg) {
        return new Response(-1, errorMsg, null);
    }
}
