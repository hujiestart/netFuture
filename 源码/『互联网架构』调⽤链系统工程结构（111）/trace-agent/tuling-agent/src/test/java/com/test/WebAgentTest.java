package com.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author Tommy
 * Created by Tommy on 2019/3/22
 **/
public class WebAgentTest {
    public static void main(String[] args) {
        // jetty web test
        try {
            Server server = new Server(8008);//设置端口号
            WebAppContext context = new WebAppContext();
            context.setContextPath("/");//访问路径
            context.setResourceBase(WebAgentTest.class.getResource("/webapp/").getPath());//路径
            context.setDescriptor(WebAgentTest.class.getResource("/webapp/WEB-INF/web.xml").getPath());//读取web.xml文件
            server.setHandler(context);
            server.start();
            System.out.println("启动成功：端口号：8008");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
