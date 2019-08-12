package com.yifeng.springboot.sprintbootdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/staticResource")
public class HelloController {

    @RequestMapping("/hello")
    public String hell() {
        return "hell";
    }

    @RequestMapping("/helloThere1")
    public String template1(){
        return "helloThere";
    }

    @RequestMapping("/helloThere2")
    @ResponseBody
    public String template2(){
        return "helloThere";
    }
}
