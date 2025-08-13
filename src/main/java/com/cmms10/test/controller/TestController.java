package com.cmms10.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test/sample")
    public String sample() {
        return "test/sample"; // templates/test/sample.html을 렌더링
    }
}
