package com.bookswag.spring.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Deprecated
@FunctionalInterface
public interface StatementStrategy {
    PreparedStatement makePreparedStatement (Connection c) throws SQLException;
}
