package com.llxs.passbook.controller;

import com.llxs.passbook.vo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
