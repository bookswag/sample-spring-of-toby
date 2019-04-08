package com.bookswag.spring.dao;

import com.bookswag.spring.database.JdbcContext;
import com.bookswag.spring.domain.User;
import lombok.NoArgsConstructor;

import java.sql.*;

@NoArgsConstructor
public class UserDao {
    private JdbcContext jdbcContext;

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(final User user) throws SQLException {
        this.jdbcContext.workWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement(
                    "insert into users(id, name, password) values(?,?,?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            return ps;
        });
    }

    public User get(final String id) throws SQLException {
        return this.jdbcContext.getUserWorkWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement(
                    "select * from users where id = ?");
            ps.setString(1, id);
            return ps;
        });
    }

    public void deleteAll() throws SQLException {
        this.jdbcContext.workWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement("delete from users");
            return ps;
        });
    }

    public int getCount() throws SQLException {
        return this.jdbcContext.getCountWorkWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement("select count(*) from users");
            return ps;
        });
    }
}
