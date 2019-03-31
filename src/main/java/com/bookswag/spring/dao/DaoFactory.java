package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;
import com.bookswag.spring.database.NConnectionMaker;

public class DaoFactory {
    public UserDao getUserDao() {
        ConnectionMaker connectionMaker = new NConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }
}
