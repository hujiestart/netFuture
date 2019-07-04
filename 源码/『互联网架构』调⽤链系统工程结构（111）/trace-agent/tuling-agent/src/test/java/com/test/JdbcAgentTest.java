package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Tommy
 * Created by Tommy on 2019/3/24
 **/
public class JdbcAgentTest {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager
                .getConnection(
                        "jdbc:mysql://192.168.31.147:3306/luban2?useUnicode=true&;characterEncoding=UTF8",
                        "root", "123456");
        PreparedStatement statment = conn
                .prepareStatement("select * from `user` ");
        ResultSet r = statment.executeQuery();
        while (r.next()) {
            System.out.println(r.getString(1) + "     " + r.getString(2));
        }
        statment.close();
        conn.close();
    }
}
