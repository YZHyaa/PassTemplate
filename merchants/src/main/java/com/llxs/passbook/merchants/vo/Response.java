package com.llxs.passbook.merchants.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("通用返回")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    @ApiModelProperty("状态码")
    private int code = 1;

    @ApiModelProperty("错误信息，正确返回空字符串")
    private String msg = "";

    @ApiModelProperty("返回值对象")
    private Object data;

    public Response data(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 空响应
     * @return
     */
    public static Response success() {
        return new Response();
    }

    /**
     * 错误响应
     * @param msg 错误信息
     * @return
     */
    public static Response failure(String msg) {
        return new Response(0, msg, null);
    }
}
