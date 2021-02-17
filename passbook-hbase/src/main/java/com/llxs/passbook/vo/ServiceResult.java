package com.llxs.passbook.vo;

import lombok.Data;

/**
 * Service 层通用返回
 */
@Data
public class ServiceResult<T> {

    boolean success;
    String message;
    T result;

    public ServiceResult(boolean success) {
        this.success = success;
    }

    public ServiceResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ServiceResult(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }


    public static ServiceResult success() {
        return new ServiceResult<>(true);
    }

    public static <T> ServiceResult success(T result) {
        ServiceResult<T> serviceResult = new ServiceResult<>(true);
        serviceResult.setResult(result);
        return serviceResult;
    }

    public static <T> ServiceResult<T> failture(String message) {
        ServiceResult<T> serviceResult = new ServiceResult<>(false);
        serviceResult.setMessage(message);
        return serviceResult;
    }

}
