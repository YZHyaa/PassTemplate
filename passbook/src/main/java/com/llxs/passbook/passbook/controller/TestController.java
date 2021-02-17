package com.llxs.passbook.passbook.controller;

import com.llxs.passbook.passbook.vo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    /**
     * 起始页
     */
    @GetMapping("/")
    String hello() {
        return "index";
    }

    /**
     * 异常演示接口
     */
    @ResponseBody
    @GetMapping("/exception")
    Response exception() throws Exception {
        throw new Exception("Welcome To llxs");
    }

}
