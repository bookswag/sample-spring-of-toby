package com.bookswag.spring.dao;

import com.bookswag.spring.database.StatementStrategy;
import com.bookswag.spring.domain.User;
import lombok.NoArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

@NoArgsConstructor
public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = statementStrategy.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {}
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {}
            }
        }
    }

    public User getUserJdbcContextWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
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
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {}
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {}
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {}
            }
        }
    }

    public int getCountJdbcContextWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
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
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {}
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {}
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {}
            }
        }
    }

    public void add(final User user) throws SQLException {
        jdbcContextWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement(
                    "insert into users(id, name, password) values(?,?,?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            return ps;
        });
    }

    public User get(final String id) throws SQLException {
        return getUserJdbcContextWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement(
                    "select * from users where id = ?");
            ps.setString(1, id);
            return ps;
        });
    }

    public void deleteAll() throws SQLException {
        jdbcContextWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement("delete from users");
            return ps;
        });
    }

    public int getCount() throws SQLException {
        return getCountJdbcContextWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement("select count(*) from users");
            return ps;
        });
    }
}
