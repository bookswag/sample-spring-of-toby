package com.bookswag.spring.dao;

import com.bookswag.spring.database.StatementStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDeleteAllStatement implements StatementStrategy {
    @Override
    public PreparedStatement makePreparedStatement (Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }
}
