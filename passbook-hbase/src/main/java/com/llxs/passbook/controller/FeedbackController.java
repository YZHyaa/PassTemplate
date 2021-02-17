package com.llxs.passbook.controller;

import com.llxs.passbook.log.LogConstants;
import com.llxs.passbook.log.LogGenerator;
import com.llxs.passbook.service.FeedbackService;
import com.llxs.passbook.vo.Feedback;
import com.llxs.passbook.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("passbook")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 创建用户评论
     * @param feedback {@link Feedback}
     * @return true/false
     */
    @PostMapping("/createFeedback")
    Response createFeedback(@RequestBody Feedback feedback) {

        LogGenerator.genLog(
                httpServletRequest,
                feedback.getUserId(),
                LogConstants.ActionName.CREATE_FEEDBACK,
                feedback
        );

        return feedbackService.createFeedback(feedback);
    }

    /**
     * 获取用户评论
     * @param userId
     * @return {@link Feedback}
     */
    @GetMapping("/getfeedback")
    Response getFeedback(Long userId) {

        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.GET_FEEDBACK,
                null
        );

        return feedbackService.getFeedback(userId);
    }
}
