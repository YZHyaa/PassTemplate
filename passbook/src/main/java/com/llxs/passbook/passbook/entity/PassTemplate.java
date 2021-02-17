package com.llxs.passbook.passbook.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * PassTemplate（优惠券） 实体对象定义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "passtemplate")
public class PassTemplate {

    /** 优惠券 id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    /** 所属商户 id（逻辑外键） */
    private int merchantId;

    /** 打折商品 id */
    private long goodsId;

    /** 折扣值（80=八折，75=七五折） */
    private int discount;

    /** 优惠券标题 */
    private String title;

    /** 优惠券摘要 */
    private String summary;

    /** 优惠券详细信息 */
    @Column(name = "`desc`")
    private String desc;

    /** 最大个数限制 */
    @Column(name = "`limit`")
    private int limit;

    /** 优惠券是否有 Token, 用于商户核销 */
    private Boolean hasToken;

    /** 优惠券背景色 */
    private Integer background;

    /** 优惠券开始时间 */
    @Column(name = "`start`")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date start;

    /** 优惠券结束时间 */
    @Column(name = "`end`")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date end;

    /** 是否通过审核（默认通过） */
    @JsonIgnore
    @Column(name = "is_audit")
    private Boolean pass;
}
