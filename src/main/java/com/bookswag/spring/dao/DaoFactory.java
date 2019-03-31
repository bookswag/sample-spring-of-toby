package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;
import com.bookswag.spring.database.NConnectionMaker;

public class DaoFactory {
    public UserDao getUserDao() {
        return new UserDao(getConnectionMaker());
    }

    public AccountDao getAccountDao() {
        return new AccountDao(getConnectionMaker());
    }

    public MessageDao getMessageDao() {
        return new MessageDao(getConnectionMaker());
    }

    private ConnectionMaker getConnectionMaker() {
        return new NConnectionMaker();
    }
}
