package com.llxs.passbook.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.passbook.dao.FeedbackDao;
import com.llxs.passbook.passbook.entity.Feedback;
import com.llxs.passbook.passbook.service.FeedbackService;
import com.llxs.passbook.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackDao feedbackDao;

    @Override
    public Response createFeedback(Feedback feedback) {

        if (!feedback.validate()) {
            log.error("Feedback Error {}", JSON.toJSONString(feedback));
            return Response.failure("Feedback Error");
        }

        feedbackDao.save(feedback);

        return Response.success();
    }

    @Override
    public Response getUserFeedback(long userId) {

        List<Feedback> feedbacks = feedbackDao.findByUserId(userId);
        return new Response(feedbacks);
    }

    @Override
    public Response getAllFeedback() {

        List<Feedback> allfeedback = feedbackDao.findAll();
        return new Response(allfeedback);
    }
}
