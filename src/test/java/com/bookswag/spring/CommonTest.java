package com.bookswag.spring;

import com.bookswag.spring.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class CommonTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonTest.class);

    @Autowired
    private ApplicationContext context;

    @Test
    public void getBeansIdForCheckingSingleton() {
        UserDao firstDao = context.getBean("userDao", UserDao.class);
        UserDao secondDao = context.getBean("userDao", UserDao.class);

        LOGGER.info("first : {}", firstDao);
        LOGGER.info("second : {}", secondDao);
        assertThat(secondDao, is(firstDao));
    }
}
