package com.bookswag.spring.dao;

import com.bookswag.spring.common.DuplicateUserIdException;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private DataSource dataSource;
    private Map<String, User> memberMap = Maps.newHashMap();

    @Before
    public void setUp() throws SQLException {
        memberMap.put("test_user1", new User("test_user1","user1_name","1234", Level.BASIC, 1, 0));
        memberMap.put("test_user2", new User("test_user2","user2_name","1234", Level.SILVER, 55, 10));
        memberMap.put("test_user3", new User("test_user3","user3_name","1234", Level.GOLD, 100, 40));
    }

    @Test(expected = DuplicateUserIdException.class)
    public void addSameValue() throws SQLException {
        Connection c = dataSource.getConnection();
        dao.add(c, memberMap.get("test_user1"));
        dao.add(c, memberMap.get("test_user1"));
    }

    @Test
    public void addAndGet() throws SQLException {
        Connection c = dataSource.getConnection();

        dao.deleteAll(c);
        assertThat(dao.getCount(c), is(0));

        User user1 = new User("spring", "bookswag", "1234", Level.SILVER, 310, 160);
        User user2 = new User("spring2", "book_swag", "1234", Level.GOLD, 200, 100);

        dao.add(c, user1);
        dao.add(c, user2);
        assertThat(dao.getCount(c), is(2));

        User dbUser1 = dao.get(c, user1.getId());
        checkSameUser(user1, dbUser1);

        User dbUser2 = dao.get(c, user2.getId());
        checkSameUser(user2, dbUser2);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void emptyGet() throws SQLException {
        Connection c = dataSource.getConnection();

        dao.deleteAll(c);
        assertThat(dao.getCount(c), is(0));
        dao.get(c,"unknown_id");
    }

    @Test
    public void getAll() throws SQLException {
        Connection c = dataSource.getConnection();

        dao.deleteAll(c);
        List<User> dbUsers0 = dao.getAll(c);
        assertThat(dbUsers0.size(), is(0));

        dao.add(c, memberMap.get("test_user1"));
        List<User> dbUsers1 = dao.getAll(c);
        assertThat(dbUsers1.size(), is(1));
        checkSameUser(memberMap.get("test_user1"), dbUsers1.get(0));

        dao.add(c, memberMap.get("test_user2"));
        List<User> dbUsers2 = dao.getAll(c);
        assertThat(dbUsers2.size(), is(2));
        checkSameUser(memberMap.get("test_user1"), dbUsers1.get(0));
        checkSameUser(memberMap.get("test_user2"), dbUsers2.get(1));

        dao.add(c, memberMap.get("test_user3"));
        List<User> dbUsers3 = dao.getAll(c);
        assertThat(dbUsers3.size(), is(3));
        checkSameUser(memberMap.get("test_user1"), dbUsers1.get(0));
        checkSameUser(memberMap.get("test_user2"), dbUsers2.get(1));
        checkSameUser(memberMap.get("test_user3"), dbUsers3.get(2));
    }

    private void checkSameUser(User member,User dbUser) {
        assertThat(member.getId(), is(dbUser.getId()));
        assertThat(member.getName(), is(dbUser.getName()));
        assertThat(member.getPassword(), is(dbUser.getPassword()));
        assertThat(member.getLevel(), is(dbUser.getLevel()));
        assertThat(member.getLogin(), is(dbUser.getLogin()));
        assertThat(member.getRecommend(), is(dbUser.getRecommend()));
    }

    @Test
    public void update() throws SQLException {
        Connection c = dataSource.getConnection();

        dao.deleteAll(c);
        assertThat(dao.getCount(c), is(0));

        User modifiedUser = memberMap.get("test_user1");
        User unchangedUser = memberMap.get("test_user2");

        dao.add(c, modifiedUser);
        dao.add(c, unchangedUser);

        modifiedUser.setName("hi");
        modifiedUser.setLevel(Level.GOLD);
        dao.update(c, modifiedUser);

        User modifiedUserAtDB = dao.get(c, modifiedUser.getId());
        checkSameUser(modifiedUser, modifiedUserAtDB);
        User unchangedUserAtDB = dao.get(c, unchangedUser.getId());
        checkSameUser(unchangedUser, unchangedUserAtDB);
    }

    @Test
    public void count() throws SQLException {
        Connection c = dataSource.getConnection();

        dao.deleteAll(c);
        assertThat(dao.getCount(c), is(0));

        dao.add(c, memberMap.get("test_user1"));
        assertThat(dao.getCount(c), is(1));

        dao.add(c, memberMap.get("test_user2"));
        assertThat(dao.getCount(c), is(2));

        dao.add(c, memberMap.get("test_user3"));
        assertThat(dao.getCount(c), is(3));
    }

}