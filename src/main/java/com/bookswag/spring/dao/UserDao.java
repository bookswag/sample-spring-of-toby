package com.bookswag.spring.dao;

import com.bookswag.spring.domain.User;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.*;

@NoArgsConstructor
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
        this.jdbcTemplate.update(c -> {
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        });
    }

    public User get(final String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id},
            (ResultSet rs, int rowNum) -> {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                return user;
        });
    }

    public void deleteAll() {
        this.jdbcTemplate.update(c -> c.prepareStatement("delete from users"));
    }

    public int getCount() {
        return this.jdbcTemplate.query(
            c -> c.prepareStatement("select count(*) from users"),
            (rs) -> { rs.next();
                return rs.getInt(1); });
    }
}
