package com.idig8.springboot;

/**
 * @program: springboot-second
 * @description: ${description}
 * @author: LiMing
 * @create: 2019-06-04 22:42
 **/

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAutoConfiguration
public class ExampleProperties {

    @Value("${teacher.id}")
    private String teacherId;

    @Value("${teacher.name}")
    private String teacherName;

    @Value("${teacher.info}")
    private String teacherInfo;

    @Value("${random.string}")
    private String randomString;

    @RequestMapping("/")
    String home() {
        return "Hello World!"+this.teacherId+":"+this.teacherName+";"+teacherInfo+"\n"+randomString;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ExampleProperties.class, args);
    }

}