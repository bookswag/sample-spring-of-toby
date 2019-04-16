package com.bookswag.spring.service;

import com.bookswag.spring.dao.UserDao;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import static com.bookswag.spring.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.bookswag.spring.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    private UserService userSerivce;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DataSource dataSource;
    private List<User> users;

    @Before
    public void setUp() {
        users = Lists.newArrayList(
            new User("test_1", "테스터1", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
            new User("test_2", "테스터2", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("test_3", "테스터3", "1234", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER+10, MIN_RECOMMEND_FOR_GOLD-1),
            new User("test_4", "테스터4", "1234", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER+10, MIN_RECOMMEND_FOR_GOLD),
            new User("test_5", "테스터5", "1234", Level.GOLD, MIN_LOGCOUNT_FOR_SILVER+50, MIN_RECOMMEND_FOR_GOLD+50)
        );
    }

    @Test
    public void bean() {
        assertThat(this.userSerivce, is(notNullValue()));
    }

    @Test
    public void upgradeUsersLevel() throws SQLException {
        Connection c = dataSource.getConnection();
        userDao.deleteAll(c);
        for (User user : users) {
            userDao.add(c, user);
        }

        userSerivce.upgradeLevels(dataSource.getConnection());

        checkLevelUpgraded(c, users.get(0), false);
        checkLevelUpgraded(c, users.get(1), true);
        checkLevelUpgraded(c, users.get(2), false);
        checkLevelUpgraded(c, users.get(3), true);
        checkLevelUpgraded(c, users.get(4), false);
    }

    @Test
    public void upgradeAllOrNothing() throws SQLException{
        Connection c = dataSource.getConnection();
        c.setAutoCommit(false);

        userDao.deleteAll(c);

        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);

        for(User user : users) {
            userDao.add(c, user);
        }

        try {
            testUserService.upgradeLevels(c);
            fail("Expected TestUserServiceException");
        } catch (TestUserServiceException e) {
            c.rollback();
        }

        checkLevelUpgraded(c, users.get(1), false);
    }

    private void checkLevelUpgraded(Connection c, User user, boolean upgraded) {
        User updatedUser = userDao.get(c, user.getId());
        if (upgraded) {
            assertThat(updatedUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(updatedUser.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void add() throws SQLException{
        Connection c = dataSource.getConnection();
        userDao.deleteAll(c);

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userSerivce.add(c, userWithLevel);
        userSerivce.add(c, userWithoutLevel);

        User userWithLevelOnDB = userDao.get(c, userWithLevel.getId());
        User userWithoutLevelOnDB = userDao.get(c, userWithoutLevel.getId());

        assertThat(userWithLevelOnDB.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelOnDB.getLevel(), is(userWithoutLevel.getLevel()));
    }

}
