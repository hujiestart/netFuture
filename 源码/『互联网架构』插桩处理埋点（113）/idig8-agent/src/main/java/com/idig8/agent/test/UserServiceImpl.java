package com.idig8.agent.test;

public class UserServiceImpl {
    public UserServiceImpl(){
        System.out.println("hello world！");
    }

    public void hello(){
//        {
//            long begin = System.currentTimeMillis(); System.out.println(begin);
//        }
        String p1 ="100";
        System.out.print("p1 = "+p1);
//        {
//            long end = System.currentTimeMillis(); System.out.println(end - begin);
//        }

    }

    public void hello$agent(){
        {
            long begin = System.currentTimeMillis();
            try{
                hello();
            }finally {
                long end = System.currentTimeMillis();
                System.out.println(end - begin);
            }
        }
    }
}
