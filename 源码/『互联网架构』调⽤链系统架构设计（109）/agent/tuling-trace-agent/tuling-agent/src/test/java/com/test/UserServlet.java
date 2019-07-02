package com.test;/**
 * Created by Administrator on 2018/5/31.
 */

import com.test.service.UserService;
import com.tuling.agent.LubanServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Tommy
 * Created by Tommy on 2018/5/31
 **/
public class UserServlet extends HttpServlet {
    Logger logger = Logger.getLogger(UserServlet.class.getName());
    private UserService userService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void init() throws ServletException {
        // 初始化dubbo 服务
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "dubbo-consumer.xml");
        context.start();
        userService = (UserService) context.getBean(UserService.class);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("hello tuling trace .this is test page");
        new LubanServer().sayHello("luban ", "luban is good man");
        try {
            // 远程调用
            userService.getUser("12", "luban");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
