package com.llxs.passbook.passbook.dao;

import com.llxs.passbook.passbook.entity.PassTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PassTemplateDao extends JpaRepository<PassTemplate, Long> {

    /**
     * 根据优惠券的 ID 查询所有优惠券
     */
    List<PassTemplate> findByIdIn(List<Long> templateIds);

    /**
     * 查找未发放完的优惠券
     */
    List<PassTemplate> findByLimitIsGreaterThan(int least);


    /**
     * 查找通过审核，且未发放完的优惠券
     */
    List<PassTemplate> findByPassIsTrueAndLimitGreaterThan(int least);


    /**
     * 根据 ID 查询优惠券
     */
    PassTemplate findById(long id);


    /**
     * 根据 merchantId 查找优惠券列表
     */
    List<PassTemplate> findByMerchantId(int merchantId);


}
