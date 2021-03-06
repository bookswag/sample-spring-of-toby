package com.bookswag.spring.dao;

import com.bookswag.spring.common.DuplicateUserIdException;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import lombok.NoArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@NoArgsConstructor
public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;

    // RowMapper callback object is stateless
    private static RowMapper<User> userRowMapper = (ResultSet rs, int rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(final User user) throws DuplicateKeyException {
        try {
            this.jdbcTemplate.update(
                "insert into users(id, name, password, email, level, login, recommend) values(?,?,?,?,?,?,?)",
                    user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
        } catch (DuplicateKeyException e) {
            throw new DuplicateUserIdException(e);
        }
    }

    @Override
    public User get(final String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, userRowMapper);
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", userRowMapper);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
            "update users set name = ?, password = ?, email = ?, level = ?, login = ?, recommend = ? where id = ? ",
            user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }

    @Override
    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForInt("select count(*) from users");
    }
}
