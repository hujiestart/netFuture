package com.idig8.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @program: springboot3d
 * @description: ${description}
 * @author: LiMing
 * @create: 2019-06-09 09:15
 **/
@Controller
public class SampleController {

    @RequestMapping("/test")
    public String testThymeleaf(ModelMap map) {
        // 设置属性
        map.put("name", "微信公众号");
        // testThymeleaf：为模板文件的名称
        // 对应src/main/resources/templates/testThymeleaf.html
        return "testThymeleaf";
    }

    @RequestMapping("/testFreemarker")
    public String testFreemarker(Map<String,String> map) {
        map.put("name", "张三");
        return "hello"; //默认为src/main/resources/templates/hello.flt
    }

}

