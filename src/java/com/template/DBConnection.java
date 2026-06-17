package com.template;
import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnection {
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/tak_limited", "root", "iwbac bic sw nimab");
    }
}