package com.bookswag.spring.database;

import com.bookswag.spring.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// StatementStrategy has to have concrete about each Dao method
public class UserAddStatement implements StatementStrategy {
    User user;

    public UserAddStatement(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement (Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        return ps;
    }
}
