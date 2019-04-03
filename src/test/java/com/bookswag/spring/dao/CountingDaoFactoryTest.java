package com.bookswag.spring.dao;

import com.bookswag.spring.database.CountingConnectionMaker;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@Deprecated
public class CountingDaoFactoryTest {
    @Test
    public void checkCount() throws ClassNotFoundException, SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);
        dao.get("spring");
        dao.get("spring");

        CountingConnectionMaker countingConnectionMaker = context.getBean("connectionMaker", CountingConnectionMaker.class);
        assertThat(countingConnectionMaker.getCount(), is(2));
    }

}