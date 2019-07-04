package com.test.service;

import java.sql.*;

/**
 * Created by Tommy on 2018/3/8.
 */
public class UserServiceImpl implements UserService {

    public User getUser(String userid, String name) {
        System.out.println("获取用户信息:" + userid);
        try {
            return selectUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User selectUser() throws Exception {
        User user = new User();
        Connection conn = DriverManager
                .getConnection(
                        "jdbc:mysql://192.168.31.147:3306/luban2?useUnicode=true&;characterEncoding=UTF8",
                        "root", "123456");
        PreparedStatement statment = conn
                .prepareStatement("select * from `user` ");
        ResultSet r = statment.executeQuery();
        while (r.next()) {
            user.setUserid(r.getString("id"));
            user.setUserName(r.getString("name"));
        }
        statment.close();
        conn.close();
        return user;
    }
}
