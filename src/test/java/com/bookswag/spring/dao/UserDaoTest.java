package com.bookswag.spring.dao;

import com.bookswag.spring.domain.User;
import com.google.common.collect.Lists;
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

import java.util.List;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test/test-applicationContext.xml")
public class UserDaoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

    @Autowired
    private UserDao dao;
    private List<User> members = Lists.newArrayList();


    @Before
    public void setUp() {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        members.add(new User("test_user1","user1_name","1234"));
        members.add(new User("test_user2","user2_name","1234"));
        members.add(new User("test_user3","user3_name","1234"));
    }

    @Test
    public void addAndGet() {
        User user1 = new User("spring", "bookswag", "1234");
        User user2 = new User("spring2", "book_swag", "1234");

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User dbUser1 = dao.get(user1.getId());
        checkSameUser(user1, dbUser1);

        User dbUser2 = dao.get(user2.getId());
        checkSameUser(user2, dbUser2);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void emptyGet() {
        dao.get("unknown_id");
    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> dbUsers0 = dao.getAll();
        assertThat(dbUsers0.size(), is(0));

        dao.add(members.get(0));
        List<User> dbUsers1 = dao.getAll();
        assertThat(dbUsers1.size(), is(1));
        checkSameUser(members.get(0), dbUsers1.get(0));

        dao.add(members.get(1));
        List<User> dbUsers2 = dao.getAll();
        assertThat(dbUsers2.size(), is(2));
        checkSameUser(members.get(0), dbUsers2.get(0));
        checkSameUser(members.get(1), dbUsers2.get(1));

        dao.add(members.get(2));
        List<User> dbUsers3 = dao.getAll();
        assertThat(dbUsers3.size(), is(3));
        checkSameUser(members.get(0), dbUsers3.get(0));
        checkSameUser(members.get(1), dbUsers3.get(1));
        checkSameUser(members.get(2), dbUsers3.get(2));
    }

    private void checkSameUser(User member,User dbUser) {
        assertThat(member.getId(), is(dbUser.getId()));
        assertThat(member.getName(), is(dbUser.getName()));
        assertThat(member.getPassword(), is(dbUser.getPassword()));
    }

    @Test
    public void count() {
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