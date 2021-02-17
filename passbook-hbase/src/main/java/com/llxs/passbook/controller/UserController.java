package com.llxs.passbook.controller;

import com.llxs.passbook.log.LogConstants;
import com.llxs.passbook.log.LogGenerator;
import com.llxs.passbook.service.UserService;
import com.llxs.passbook.vo.Response;
import com.llxs.passbook.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/passbook")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 创建用户
     * @param user {@link User}
     * @return userId
     */
    @PostMapping("/createuser")
    public Response createUser(@RequestBody User user) {

        LogGenerator.genLog(
                httpServletRequest,
                -1L,
                LogConstants.ActionName.CREATE_USER,
                user
        );

        return userService.createUser(user);
    }

}
