package com.llxs.passbook.passbook.controller;

import com.llxs.passbook.passbook.config.security.AccessContext;
import com.llxs.passbook.passbook.entity.Feedback;
import com.llxs.passbook.passbook.log.LogConstants;
import com.llxs.passbook.passbook.log.LogGenerator;
import com.llxs.passbook.passbook.service.FeedbackService;
import com.llxs.passbook.passbook.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/passbook")
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
    @PostMapping("/feedback")
    Response createFeedback(@RequestBody Feedback feedback) {

        LogGenerator.genLog(
                httpServletRequest,
                AccessContext.getUserId(),
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
    @GetMapping("/feedback/{userId}")
    Response getUserFeedback(@PathVariable("userId") long userId) {

        LogGenerator.genLog(
                httpServletRequest,
                AccessContext.getUserId(),
                LogConstants.ActionName.GET_FEEDBACK,
                null
        );

        return feedbackService.getUserFeedback(userId);
    }

    /**
     * 获取全部反馈信息
     */
    @GetMapping("/feedback")
    Response getAllFeedback() {
        return feedbackService.getAllFeedback();
    }
}
