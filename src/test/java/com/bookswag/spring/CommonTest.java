package com.bookswag.spring;

import com.bookswag.spring.dao.DaoFactory;
import com.bookswag.spring.dao.UserDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class CommonTest {
    @Test
    public void getBeansIdForCheckingSingleton() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        UserDao firstDao = context.getBean("userDao", UserDao.class);
        UserDao secondDao = context.getBean("userDao", UserDao.class);

        System.out.println(firstDao);
        System.out.println(secondDao);
        assertThat(secondDao, is(firstDao));
    }
}
