package com.bookswag.spring.dao;

import com.bookswag.spring.domain.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

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
    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User newUser = new User();
        newUser.setId("spring");
        newUser.setName("bookswag");
        newUser.setPassword("1234");

        dao.add(newUser);
        assertThat(dao.getCount(), is(1));

        User dbUser = dao.get(newUser.getId());

        assertThat(dbUser.getName(), is(newUser.getName()));
        assertThat(dbUser.getPassword(), is(newUser.getPassword()));
    }

    @Test
    public void count() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

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