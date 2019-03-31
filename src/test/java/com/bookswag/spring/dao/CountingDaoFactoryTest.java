package com.bookswag.spring.dao;

import com.bookswag.spring.database.CountingConnectionMaker;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CountingDaoFactoryTest {
    @Test
    public void checkCount() throws ClassNotFoundException, SQLException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);
        dao.get("spring");
        dao.get("spring");

        CountingConnectionMaker countingConnectionMaker = context.getBean("connectionMaker", CountingConnectionMaker.class);
        assertThat(countingConnectionMaker.getCount(), is(2));
    }

}