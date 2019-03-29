package com.bookswag.spring.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NUserDao extends UserDao {
    private static final String JDBC_URL = "jdbc:mysql://localhost/toby";
    private static final String LOCAL_USER = "user01";
    private static final String LOCAL_PASSWORD = "1234";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection( JDBC_URL, LOCAL_USER, LOCAL_PASSWORD );
        return c;
    }
}
