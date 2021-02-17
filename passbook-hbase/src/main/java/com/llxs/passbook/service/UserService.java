package com.llxs.passbook.service;

import com.llxs.passbook.vo.Response;
import com.llxs.passbook.vo.User;

public interface UserService {

    /**
     * 创建 User
     */
    Response createUser(User user);
}
