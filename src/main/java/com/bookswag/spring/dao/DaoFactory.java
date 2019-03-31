package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;
import com.bookswag.spring.database.NConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(getConnectionMaker());
    }

    @Bean
    public AccountDao accountDao() {
        return new AccountDao(getConnectionMaker());
    }

    @Bean
    public MessageDao messageDao() {
        return new MessageDao(getConnectionMaker());
    }

    private ConnectionMaker getConnectionMaker() {
        return new NConnectionMaker();
    }
}
