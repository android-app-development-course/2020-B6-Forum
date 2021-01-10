package com.example.demo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldTest {
    @RequestMapping("/hello")
    public String Hello(){
        return "hello worl";
    }
}
