package com.bookswag.spring.dao;

import com.bookswag.spring.database.JdbcContext;
import com.bookswag.spring.domain.User;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;

@NoArgsConstructor
public class UserDao {
    private JdbcContext jdbcContext;

    public void setDataSource(DataSource dataSource) {
        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);
    }

    public void add(final User user) throws SQLException {
        executeAddSql("insert into users(id, name, password) values(?,?,?)", user);
    }

    private void executeAddSql(final String query, final User user) throws SQLException {
        this.jdbcContext.workWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            return ps;
        });
    }

    public User get(final String id) throws SQLException {
        return executeGetUserSql("select * from users where id = ?", id);
    }

    private User executeGetUserSql(final String query, final String param) throws SQLException {
        return this.jdbcContext.getUserWorkWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, param);
            return ps;
        });
    }

    public void deleteAll() throws SQLException {
        executeSql("delete from users");
    }

    private void executeSql(final String query) throws SQLException {
        this.jdbcContext.workWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement(query);
            return ps;
        });
    }

    public int getCount() throws SQLException {
        return executeGetCountSql("select count(*) from users");
    }

    private int executeGetCountSql(final String query) throws SQLException {
        return this.jdbcContext.getCountWorkWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement(query);
            return ps;
        });
    }
}
