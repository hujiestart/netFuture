package com.idig8.api.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 请求分发器
 * @author idig8.com
 *
 */
public class ApiGatewayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    ApplicationContext context;
    private ApiGatewayHandler apiHandler;

    @Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        apiHandler = context.getBean(ApiGatewayHandler.class);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        apiHandler.handle(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        apiHandler.handle(request, response);
    }
}
