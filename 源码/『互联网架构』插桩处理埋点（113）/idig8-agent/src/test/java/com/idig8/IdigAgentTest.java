package com.idig8;

        import com.idig8.agent.test.UserServiceImpl;

        import java.lang.instrument.Instrumentation;

public class IdigAgentTest {
    public static void main(String[] args) {
        System.out.println("hello world idig8!");
        UserServiceImpl userService = new UserServiceImpl();
        userService.hello();
    }
}
