package com.llxs.passbook.service.impl;

import com.llxs.passbook.constant.Constants;
import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.service.UserService;
import com.llxs.passbook.utils.RowKeyGenUtil;
import com.llxs.passbook.vo.Response;
import com.llxs.passbook.vo.User;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Response createUser(User user) {

        byte[] FAMILY_B = HBaseTable.UserTable.FAMILY_B.getBytes();
        byte[] NAME = HBaseTable.UserTable.NAME.getBytes();
        byte[] AGE = HBaseTable.UserTable.AGE.getBytes();
        byte[] SEX = HBaseTable.UserTable.SEX.getBytes();

        byte[] FAMILY_O = HBaseTable.UserTable.FAMILY_O.getBytes();
        byte[] PHONE = HBaseTable.UserTable.PHONE.getBytes();
        byte[] ADDRESS = HBaseTable.UserTable.ADDRESS.getBytes();

        // 根据当前用户数量生成 RowKey
        Long curCount = redisTemplate.opsForValue().increment(Constants.USE_COUNT_REDIS_KEY, 1);
        Long userId = RowKeyGenUtil.genUserId(curCount);

        Put put = new Put(Bytes.toBytes(userId));

        put.addColumn(FAMILY_B, NAME, Bytes.toBytes(user.getBaseInfo().getName()));
        put.addColumn(FAMILY_B, AGE, Bytes.toBytes(user.getBaseInfo().getAge()));

        put.addColumn(FAMILY_B, NAME, Bytes.toBytes(user.getBaseInfo().getName()));
        put.addColumn(FAMILY_B, AGE, Bytes.toBytes(user.getBaseInfo().getAge()));
        put.addColumn(FAMILY_B, SEX, Bytes.toBytes(user.getBaseInfo().getSex()));

        put.addColumn(FAMILY_O, PHONE, Bytes.toBytes(user.getOtherInfo().getPhone()));
        put.addColumn(FAMILY_O, ADDRESS, Bytes.toBytes(user.getOtherInfo().getAddress()));


        hbaseTemplate.saveOrUpdate(HBaseTable.UserTable.TABLE_NAME, put);

        return new Response(userId);
    }
}
