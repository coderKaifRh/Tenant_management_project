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

        // Commonly used default MySQL passwords
        String[] passwords = {
            System.getProperty("db.password", "iwbac bic sw nimab"),
            "",             // XAMPP / WAMP default (no password)
            "root",         // Default MySQL Workbench password
            "1234",
            "123456",
            "admin"
        };

        String user = System.getProperty("db.user", "root");
        String baseUrl = "jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true&useSSL=false";

        Exception lastEx = null;
        for (String pwd : passwords) {
            try {
                // 1. Connect and ensure database tak_limited exists
                try (Connection rawConn = DriverManager.getConnection(baseUrl, user, pwd);
                     Statement stmt = rawConn.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS tak_limited");
                }

                // 2. Connect to tak_limited
                String dbUrl = "jdbc:mysql://localhost:3306/tak_limited?allowPublicKeyRetrieval=true&useSSL=false&allowMultiQueries=true";
                Connection conn = DriverManager.getConnection(dbUrl, user, pwd);

                // 3. Auto-initialize tables if missing
                initSchema(conn);

                connection = conn;
                return connection;
            } catch (Exception e) {
                lastEx = e;
            }
        }

        throw lastEx != null ? lastEx : new Exception("Unable to connect to MySQL database.");
    }

    private static void initSchema(Connection conn) {
        try (Statement checkStmt = conn.createStatement()) {
            // Check if tables already exist
            try {
                checkStmt.executeQuery("SELECT 1 FROM users LIMIT 1");
                return; // Schema is already set up!
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
                        try (Statement s = conn.createStatement()) {
                            s.execute(trimmedQuery);
                        } catch (Exception ignored) {}
                    }
                }
            }
        } catch (Exception ignored) {}
    }
}