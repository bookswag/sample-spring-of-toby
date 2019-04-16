package com.bookswag.spring.dao;

import com.bookswag.spring.domain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void add(Connection c, User user);
    void update(Connection c, User user) throws SQLException;
    User get(Connection c, String id);
    List<User> getAll(Connection c);
    void deleteAll(Connection c);
    int getCount(Connection c);
}
