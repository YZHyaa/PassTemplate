package com.llxs.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一错误信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo<T> {

    public static final Integer ERROR = -1;

    /** 特定错误码 */
    private Integer code;

    /** 错误信息 */
    private String message;

    /** 请求 url */
    private String url;

    /** 请求返回的数据 */
    private T data;
}
