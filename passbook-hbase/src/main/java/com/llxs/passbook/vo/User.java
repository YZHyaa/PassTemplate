package com.llxs.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HBase 的 UserTable 实体定义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /** 用户 ID（存入HBase时的RowKey）*/
    private Long id;

    /** 用户基本信息 */
    private BaseInfo baseInfo;

    /** 用户其余信息 */
    private OtherInfo otherInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BaseInfo {

        private String name;
        private Integer age;
        private String sex;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OtherInfo {
        private String phone;
        private String address;
    }
}
