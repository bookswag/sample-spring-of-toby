package com.bookswag.spring.learningtest;

import com.bookswag.spring.dao.UserDao;
import com.bookswag.spring.dao.UserDaoJdbc;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import com.bookswag.spring.learningtest.factorybean.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test/test-applicationContext.xml")
public class CommonTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonTest.class);
    private static final String TEST_EMAIL = "";


    @Autowired
    private ApplicationContext context;
    @Autowired
    private DataSource dataSource;
    private UserDao userDao;

    @Before
    public void setUp() {
        userDao = context.getBean("userDao", UserDaoJdbc.class);
    }

    @Test
    public void getBeansIdForCheckingSingleton() {
        UserDao firstDao = context.getBean("userDao", UserDaoJdbc.class);
        UserDao secondDao = context.getBean("userDao", UserDaoJdbc.class);

        LOGGER.info("first : {}", firstDao);
        LOGGER.info("second : {}", secondDao);
        assertThat(secondDao, is(firstDao));
    }

    @Test
    public void sqlExceptionTranslate() {
        userDao.deleteAll();

        User user1 = new User("test_id", "test_name", "1234", TEST_EMAIL, Level.BASIC, 1, 0);
        try {
            userDao.add(user1);
            userDao.add(user1);
        } catch(DuplicateKeyException e) {
            SQLException sqlException = (SQLException) e.getCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

            // TODO : Search way to compile
            //assertThat(set.translate(null, null, sqlException), isA(DuplicateKeyException.class));
        }
    }

    @Test
    public void getBeanMessage() {
        Object message = context.getBean("message");
        assertThat(((Message) message).getText(), is("Factory Bean"));
    }
}
