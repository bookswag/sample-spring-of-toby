package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;
import com.bookswag.spring.database.CountingConnectionMaker;
import com.bookswag.spring.database.NConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {
    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(actualConnectionMaker());
    }

    @Bean
    public ConnectionMaker actualConnectionMaker() {
        return new NConnectionMaker();
    }
}
