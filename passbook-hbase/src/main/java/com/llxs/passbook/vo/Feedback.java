package com.llxs.passbook.vo;

import com.google.common.base.Enums;
import com.llxs.passbook.constant.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HBase 的 Feedback（用户反馈）实体对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    /** 用户 id */
    private Long userId;

    /** 评论类型 */
    private String type;

    /** PassTemplate RowKey, 如果是 app 类型的评论, 则没有 */
    private String templateId;

    /** 评论内容 */
    private String comment;

    public boolean validate() {

        FeedbackType feedbackType = Enums.getIfPresent(
                FeedbackType.class, this.type.toUpperCase()
        ).orNull();

        return !(null == feedbackType || null == comment);
    }
}
