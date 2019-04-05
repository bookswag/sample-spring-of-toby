package com.bookswag.spring.dao;

import com.bookswag.spring.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * progress of workflow on JDBC
 * 1. Get 'Connection' for connection between APP with DB
 * 2. Make up 'Statement(or, PreparedStatement)' has SQL
 * 3. Execute that 'Statement'
 * 4. in view case, Put the result of Query execution to 'ResultSet' & Move to Object (in this case, User)
 * 5. Must Close all resource such as Connection, Statement, ResultSet at last time
 * 6. Handle exception from JDBC API, or Declare 'throws' to method to do throw out of it
 */
public class UserDaoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

    // Not @Autowired
    private UserDao dao;

    @Before
    public void setUp() throws SQLException {
        dao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource( // independent connection, not share
            "jdbc:mysql://localhost/toby_test?autoReconnect=true&amp;useSSL=false", "user01", "1234", true);
        dao.setDataSource(dataSource);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
    }

    @Test
    public void addAndGet() throws SQLException {
        User user1 = new User("spring", "bookswag", "1234");
        User user2 = new User("spring2", "book_swag", "1234");

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User dbUser1 = dao.get(user1.getId());
        assertThat(dbUser1.getName(), is(user1.getName()));
        assertThat(dbUser1.getPassword(), is(user1.getPassword()));

        User dbUser2 = dao.get(user2.getId());
        assertThat(dbUser2.getName(), is(user2.getName()));
        assertThat(dbUser2.getPassword(), is(user2.getPassword()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void emptyGet() throws SQLException {
        dao.get("unknown_id");
    }

    @Test
    public void count() throws SQLException {
        User user1 = new User("id1", "user1", "1234");
        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        User user2 = new User("id2", "user2", "1234");
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User user3 = new User("id3", "user3", "1234");
        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

}