package com.bookswag.spring.service;

import com.bookswag.spring.dao.UserDao;
import com.bookswag.spring.database.TransactionHandler;
import com.bookswag.spring.database.UserServiceTx;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import static com.bookswag.spring.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.bookswag.spring.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test/test-applicationContext.xml")
@DirtiesContext
public class UserServiceTest {
    private static final String TEST_EMAIL = "test_spring_of_toby@gmail.com";

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private List<User> users;

    @Before
    public void setUp() {
        users = Lists.newArrayList(
            new User("test_1", "테스터1", "1234", TEST_EMAIL, Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
            new User("test_2", "테스터2", "1234", TEST_EMAIL, Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("test_3", "테스터3", "1234", TEST_EMAIL, Level.SILVER, MIN_LOGCOUNT_FOR_SILVER+10, MIN_RECOMMEND_FOR_GOLD-1),
            new User("test_4", "테스터4", "1234", TEST_EMAIL, Level.SILVER, MIN_LOGCOUNT_FOR_SILVER+10, MIN_RECOMMEND_FOR_GOLD),
            new User("test_5", "테스터5", "1234", TEST_EMAIL, Level.GOLD, MIN_LOGCOUNT_FOR_SILVER+50, MIN_RECOMMEND_FOR_GOLD+50)
        );

        userDao.deleteAll();
    }

    @Test
    public void bean() {
        assertThat(this.userService, is(notNullValue()));
    }

    /**
     * Isolation testing
     */
    @Test
    public void upgradeUsersLevel() throws Exception{
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);
        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);


        userServiceImpl.upgradeLevels();

        List<User> updatedUsers = mockUserDao.getUpdatedUsers();
        assertThat(updatedUsers.size(), is(2));
        checkUserAndLevel(updatedUsers.get(0), "test_2", Level.SILVER);
        checkUserAndLevel(updatedUsers.get(1), "test_4", Level.GOLD);
        
        List<String> requests = mockMailSender.getRequests();
        assertThat(requests.size(), is(2));
        assertThat(requests.get(0), is(users.get(1).getEmail()));
        assertThat(requests.get(1), is(users.get(3).getEmail()));
    }

    private void checkUserAndLevel(User updatedUser, String expectedId, Level expectedLevel) {
        assertThat(updatedUser.getId(), is(expectedId));
        assertThat(updatedUser.getLevel(), is(expectedLevel));
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        MockMailSender mockMailSender = new MockMailSender();
        UserServiceImpl testUserService = new TestUserServiceImpl(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setMailSender(mockMailSender);

        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(testUserService);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern("upgradeLevels");

        UserService txUserService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { UserService.class },
                txHandler
        );

        userDao.deleteAll();
        for(User user : users) {
            userDao.add(user);
        }

        try {
            txUserService.upgradeLevels();
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

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelOnDB = userDao.get(userWithLevel.getId());
        User userWithoutLevelOnDB = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelOnDB.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelOnDB.getLevel(), is(userWithoutLevel.getLevel()));
    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updatedUsers = Lists.newArrayList();

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdatedUsers() {
            return this.updatedUsers;
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void update(User user) {
            updatedUsers.add(user);
        }

        @Override
        public void add(User user) { throw new UnsupportedOperationException(); }
        @Override
        public void deleteAll() { throw new UnsupportedOperationException(); }
        @Override
        public User get(String id) { throw new UnsupportedOperationException(); }
        @Override
        public int getCount() { throw new UnsupportedOperationException(); }
    }

    static class MockMailSender implements MailSender {
        private List<String> requests = Lists.newArrayList();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage messege) throws MailException {
            requests.add(messege.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage[] messages) throws MailException {
            throw new UnsupportedOperationException();
        }
    }
}
