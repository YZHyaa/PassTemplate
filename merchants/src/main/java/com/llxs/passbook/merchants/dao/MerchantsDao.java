package com.llxs.passbook.merchants.dao;


import com.llxs.passbook.merchants.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * MerchantsDao 接口
 * */
@Component
public interface MerchantsDao extends JpaRepository<Merchants, Integer> {

    /**
     * 根据商户名称获取商户对象
     * @param name 商户名称
     * @return {@link Merchants}
     * */
    Merchants findByName(String name);

    /**
     * 根据 id 获取商户对象
     * @param id 商户 id
     * @return {@link Merchants}
     * */
    Merchants findByCid(Integer id);

}
