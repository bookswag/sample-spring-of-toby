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

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    private UserService userSerivce;
    @Autowired
    private UserDao userDao;
    private List<User> users;

    @Before
    public void setUp() {
        users = Lists.newArrayList(
            new User("test_1", "테스터1", "1234", Level.BASIC, 49, 0),
            new User("test_2", "테스터2", "1234", Level.BASIC, 50, 0),
            new User("test_3", "테스터3", "1234", Level.SILVER, 60, 29),
            new User("test_4", "테스터4", "1234", Level.SILVER, 60, 30),
            new User("test_5", "테스터5", "1234", Level.GOLD, 100, 100)
        );

        userDao.deleteAll();
    }

    @Test
    public void bean() {
        assertThat(this.userSerivce, is(notNullValue()));
    }

    @Test
    public void upgradeUsersLevel() {
        for (User user : users) {
            userDao.add(user);
        }

        userSerivce.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    @Test
    public void add() {
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userSerivce.add(userWithLevel);
        userSerivce.add(userWithoutLevel);

        User userWithLevelOnDB = userDao.get(userWithLevel.getId());
        User userWithoutLevelOnDB = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelOnDB.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelOnDB.getLevel(), is(userWithoutLevel.getLevel()));
    }

    private void checkLevel(User user, Level expectedLevel) {
        User updatedUser = userDao.get(user.getId());
        assertThat(updatedUser.getLevel(), is(expectedLevel));
    }
}
