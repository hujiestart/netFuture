package com.idig8.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: springboot-first
 * @description: ${description}
 * @author: LiMing
 * @create: 2019-06-04 22:09
 **/

@Controller
public class HelloController {

    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "Hello world!";
    }
}
