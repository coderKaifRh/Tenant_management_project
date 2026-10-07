package com.template;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {
    private static Connection connection = null;

    public static synchronized Connection getConnection() throws Exception {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        // 1. First, try connecting to MySQL (if user has MySQL or XAMPP installed)
        try {
            Connection mysqlConn = tryConnectMySQL();
            if (mysqlConn != null) {
                System.out.println("[DB] Successfully connected to MySQL server.");
                connection = mysqlConn;
                return connection;
            }
        } catch (Exception e) {
            System.out.println("[DB] MySQL not running or not found. Falling back to embedded zero-setup database...");
        }

        // 2. Fallback: Zero-Setup Embedded Database (Works without ANY MySQL installation!)
        try {
            Class.forName("org.h2.Driver");
            String h2Url = "jdbc:h2:./tak_limited_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE;AUTO_SERVER=TRUE";
            Connection h2Conn = DriverManager.getConnection(h2Url, "sa", "");
            System.out.println("[DB] Connected to embedded persistent database (No MySQL needed!).");
            initSchema(h2Conn);
            connection = h2Conn;
            return connection;
        } catch (Exception ex) {
            System.err.println("[DB ERROR] Failed to initialize embedded database: " + ex.getMessage());
            throw ex;
        }
    }

    private static Connection tryConnectMySQL() {
        String[] passwords = {
            System.getProperty("db.password", "iwbac bic sw nimab"),
            "",             // XAMPP default
            "root",         // Common default
            "1234",
            "123456",
            "admin"
        };

        String user = System.getProperty("db.user", "root");
        String baseUrl = "jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true&useSSL=false&connectTimeout=2000";

        for (String pwd : passwords) {
            try {
                try (Connection rawConn = DriverManager.getConnection(baseUrl, user, pwd);
                     Statement stmt = rawConn.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS tak_limited");
                }

                String dbUrl = "jdbc:mysql://localhost:3306/tak_limited?allowPublicKeyRetrieval=true&useSSL=false&allowMultiQueries=true&connectTimeout=2000";
                Connection conn = DriverManager.getConnection(dbUrl, user, pwd);
                initSchema(conn);
                return conn;
            } catch (Exception ignored) {
                // Try next password
            }
        }
        return null;
    }

    private static void initSchema(Connection conn) {
        try (Statement checkStmt = conn.createStatement()) {
            // Check if tables already exist
            try {
                checkStmt.executeQuery("SELECT 1 FROM users LIMIT 1");
                return; // Schema already exists
            } catch (Exception ignored) {
                // Table doesn't exist yet, proceed to create
            }

            InputStream in = DBConnection.class.getResourceAsStream("/schema.sql");
            if (in == null) {
                in = DBConnection.class.getClassLoader().getResourceAsStream("schema.sql");
            }
            if (in != null) {
                StringBuilder sb = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String trimmed = line.trim();
                        if (!trimmed.startsWith("--") && !trimmed.startsWith("/*")) {
                            sb.append(line).append("\n");
                        }
                    }
                }
                String[] queries = sb.toString().split(";");
                for (String q : queries) {
                    String trimmedQuery = q.trim();
                    if (!trimmedQuery.isEmpty()) {
                        // Skip database level commands for embedded engine
                        String upper = trimmedQuery.toUpperCase();
                        if (upper.startsWith("CREATE DATABASE") || upper.startsWith("USE ")) {
                            continue;
                        }
                        try (Statement s = conn.createStatement()) {
                            s.execute(trimmedQuery);
                        } catch (Exception ex) {
                            // Suppress individual creation errors
                        }
                    }
                }
                System.out.println("[DB] Database tables initialized successfully.");
            }
        } catch (Exception ignored) {}
    }
}