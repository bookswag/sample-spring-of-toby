package com.bookswag.spring.database;

import com.bookswag.spring.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcContext {
    private DataSource dataSource;
    private Connection connection;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void executeSql(final String query) throws SQLException {
        this.workWithStatementStrategy(c -> {
            PreparedStatement ps = c.prepareStatement(query);
            return ps;
        });
    }

    public void executeAddSql(final String query, final User user) throws SQLException {
        this.workWithStatementStrategy(c -> {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            return ps;
        });
    }

    public void workWithStatementStrategy (StatementStrategy statementStrategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = this.connection;
            ps = statementStrategy.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
            if (c != null) { try { c.close(); } catch (SQLException e) {} }
        }
    }

    public int executeGetCountSql(final String query) throws SQLException {
        return this.getCountWorkWithStatementStrategy(c -> {
            PreparedStatement ps = c.prepareStatement(query);
            return ps;
        });
    }

    public int getCountWorkWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = statementStrategy.makePreparedStatement(c);

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
            if (c != null) { try { c.close(); } catch (SQLException e) {} }
        }
    }

    public User executeGetUserSql(final String query, final String param) throws SQLException {
        return this.getUserWorkWithStatementStrategy(c -> {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, param);
            return ps;
        });
    }

    public User getUserWorkWithStatementStrategy (StatementStrategy statementStrategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = statementStrategy.makePreparedStatement(c);

            rs = ps.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            if (user == null) {
                throw new EmptyResultDataAccessException(1);
            }
            return user;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
            if (c != null) { try { c.close(); } catch (SQLException e) {} }
        }
    }
}
