package com.llxs.passbook.passbook.service;

import com.llxs.passbook.passbook.entity.Feedback;
import com.llxs.passbook.passbook.vo.Response;

public interface FeedbackService {

    Response createFeedback(Feedback feedback);

    Response getUserFeedback(long userId);

    Response getAllFeedback();
}
