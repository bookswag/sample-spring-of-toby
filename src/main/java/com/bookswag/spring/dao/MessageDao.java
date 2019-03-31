package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;

/**
 * Dummy class for big DaoFactory
 */
public class MessageDao {
    private ConnectionMaker connectionMaker;

    public MessageDao() {
        DaoFactory daoFactory = new DaoFactory();
        this.connectionMaker = daoFactory.connectionMaker();
    }
}
