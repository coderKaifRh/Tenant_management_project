package com.template;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String DB_URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/tak_limited?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true");
    private static final String DB_USER = System.getProperty("db.user", "root");
    private static final String DB_PASSWORD = System.getProperty("db.password", "iwbac bic sw nimab");

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}