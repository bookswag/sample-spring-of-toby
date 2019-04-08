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
        jdbcContext.executeAddSql("insert into users(id, name, password) values(?,?,?)", user);
    }

    public User get(final String id) throws SQLException {
        return jdbcContext.executeGetUserSql("select * from users where id = ?", id);
    }

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("delete from users");
    }

    public int getCount() throws SQLException {
        return jdbcContext.executeGetCountSql("select count(*) from users");
    }
}
