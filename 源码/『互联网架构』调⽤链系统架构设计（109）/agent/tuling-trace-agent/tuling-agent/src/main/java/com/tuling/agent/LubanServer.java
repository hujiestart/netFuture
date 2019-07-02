package com.tuling.agent;

/**
 * @author Tommy
 * Created by Tommy on 2019/3/20
 **/
public class LubanServer {

    public Integer sayHello(String name, String message) {
        System.out.println("hello V2.0");
        return 0;
    }

    public void hiHello(String name, String message) {
        System.out.println("hello V2.0");
    }

    public static void hello(String name, String message) {
        System.out.println("hello V2.0");
    }

    public String append(String name, String message) {
        return name + message;
    }

    public Object getInt() {
        return 1;
    }
}
