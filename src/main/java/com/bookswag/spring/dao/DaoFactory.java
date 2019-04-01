package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;
import com.bookswag.spring.database.NConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

//    DI through method
//    @Bean
//    public UserDao userDao() {
//        UserDao userDao = new UserDao();
//        userDao.setConnectionMaker(connectionMaker());
//        return return userDao;
//    }

    @Bean
    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }

    @Bean
    public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }

    /**
     *
     * It can do it toby said
     * in develop
     *  return new LocalDBConnectionMaker();
     * in production
     *  return ProductionDBConnectionMaker();
     *  but it's tough to change that code whenever deploy.
     * @return
     */
    @Bean
    public ConnectionMaker connectionMaker() {
        return new NConnectionMaker();
    }
}
