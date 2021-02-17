package com.llxs.passbook.mapper;

import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.vo.User;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 根据查询结果手动 ORM（转为 User）
 */
public class UserRowMapper implements RowMapper<User> {

    private static byte[] FAMILY_B = HBaseTable.UserTable.FAMILY_B.getBytes();
    private static byte[] NAME = HBaseTable.UserTable.NAME.getBytes();
    private static byte[] AGE = HBaseTable.UserTable.AGE.getBytes();
    private static byte[] SEX = HBaseTable.UserTable.SEX.getBytes();

    private static byte[] FAMILY_O = HBaseTable.UserTable.FAMILY_O.getBytes();
    private static byte[] PHONE = HBaseTable.UserTable.PHONE.getBytes();
    private static byte[] ADDRESS = HBaseTable.UserTable.ADDRESS.getBytes();

    @Override
    public User mapRow(Result result, int rowNum) throws Exception {

        // 构建 B(BaseInfo) 列族
        User.BaseInfo baseInfo = new User.BaseInfo(
                Bytes.toString(result.getValue(FAMILY_B, NAME)),
                Bytes.toInt(result.getValue(FAMILY_B, AGE)),
                Bytes.toString(result.getValue(FAMILY_B, SEX))
        );

        // 构建 O(OtherInfo) 列族
        User.OtherInfo otherInfo = new User.OtherInfo(
                Bytes.toString(result.getValue(FAMILY_O, PHONE)),
                Bytes.toString(result.getValue(FAMILY_O, ADDRESS))
        );

        return new User(
                Bytes.toLong(result.getRow()), baseInfo, otherInfo
        );
    }
}
