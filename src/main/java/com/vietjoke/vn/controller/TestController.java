package com.vietjoke.vn.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/saveHello")
    public String saveHello(HttpSession session) {
        session.setAttribute("hello", "world");
        return "hello";
    }

    @GetMapping("/saveHello2")
    public String saveHello() {
        return "hello2";
    }

    @GetMapping("/getHello")
    public String getHello(HttpSession session) {
        return (String) session.getAttribute("hello");
    }

}
