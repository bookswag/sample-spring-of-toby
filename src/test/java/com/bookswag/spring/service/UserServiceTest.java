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
import org.springframework.transaction.PlatformTransactionManager;

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
    private PlatformTransactionManager transactionManager;
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

        userDao.deleteAll();
    }

    @Test
    public void bean() {
        assertThat(this.userSerivce, is(notNullValue()));
    }

    @Test
    public void upgradeUsersLevel() throws Exception{
        for (User user : users) {
            userDao.add(user);
        }

        userSerivce.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(this.transactionManager);
        userDao.deleteAll();
        for(User user : users) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("Expected TestUserServiceException");
        } catch (TestUserServiceException e) { }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updatedUser = userDao.get(user.getId());
        if (upgraded) {
            assertThat(updatedUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(updatedUser.getLevel(), is(user.getLevel()));
        }
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

}
