package com.llxs.passbook.passbook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Pass（优惠券使用信息） 实体对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pass")
public class Pass {

    /** 使用记录 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    /** 用户 id */
    @Column(name = "userId")
    private long userId;

    /** PassTemplate ID */
    @Column(name = "templateId")
    private long templateId;

    /** 优惠券 token, 有可能是 null（无token）*/
    @Column(name = "token")
    private String token;

    /** 领取日期 */
    @Column(name = "assignedDate")
    private Date assignedDate;

    /** 消费日期, 不为空代表已经被消费了 */
    @Column(name = "conDate")
    private Date conDate;
}
