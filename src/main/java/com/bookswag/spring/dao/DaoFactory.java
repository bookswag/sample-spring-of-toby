package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;
import com.bookswag.spring.database.NConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    @Deprecated
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    @Deprecated
    public AccountDao accountDao() {
        return new AccountDao();
    }

    @Bean
    @Deprecated
    public MessageDao messageDao() {
        return new MessageDao();
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new NConnectionMaker();
    }
}
