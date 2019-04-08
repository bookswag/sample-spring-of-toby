package com.bookswag.spring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoDeleteAll extends UserDao {
    // NOT GOOD
    // - it has to have a lot of new class about each DAO logic
    // - Extension structure has to be fixed on class design. there would be less flexibility.
    @Override
    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps;
        ps = c.prepareStatement("delete from users");
        return ps;
    }
}
