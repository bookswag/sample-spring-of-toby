package com.bookswag.spring.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DUserDao extends UserDao {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return null; // can return another DB connection
    }
}
