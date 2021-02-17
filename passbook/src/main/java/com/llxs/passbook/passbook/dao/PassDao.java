package com.llxs.passbook.passbook.dao;

import com.llxs.passbook.passbook.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface PassDao extends JpaRepository<Pass, Long> {

    /**
     * 根据 UserId 查优惠券记录
     */
    List<Pass> findByUserId(long userId);

    /**
     *  根据 UserId 和使用日期查优惠券记录
     */
    List<Pass> findByUserIdAndConDate(long userId, Date conDate);

    /**
     * 根据 userId 和 templated 查找优惠券记录
     */
    List<Pass> findByUserIdAndTemplateId(long userId, long templateId);

}
