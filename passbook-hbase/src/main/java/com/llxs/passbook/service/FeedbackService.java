package com.llxs.passbook.service;

import com.llxs.passbook.vo.Feedback;
import com.llxs.passbook.vo.Response;

public interface FeedbackService {

    /**
     * 创建评论
     */
    Response createFeedback(Feedback feedback);

    /**
     * 获取用户评论
     */
    Response getFeedback(Long userId);
}
