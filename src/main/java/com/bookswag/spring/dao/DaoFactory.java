package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;
import com.bookswag.spring.database.NConnectionMaker;

public class DaoFactory {
    public UserDao getUserDao() {
        return new UserDao(new NConnectionMaker());
    }

    public AccountDao getAccountDao() {
        return new AccountDao(new NConnectionMaker());
    }

    public MessageDao getMessageDao() {
        return new MessageDao(new NConnectionMaker());
    }
}
