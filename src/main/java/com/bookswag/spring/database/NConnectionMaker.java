package com.bookswag.spring.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Deprecated
public class NConnectionMaker implements ConnectionMaker {
    private static final String JDBC_URL = "jdbc:mysql://localhost/toby";
    private static final String LOCAL_USER = "user01";
    private static final String LOCAL_PASSWORD = "1234";

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection( JDBC_URL, LOCAL_USER, LOCAL_PASSWORD );
        return c;
    }
}
