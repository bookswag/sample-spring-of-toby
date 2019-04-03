package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;

/**
 * Dummy class for big DaoFactory
 */
@Deprecated
public class MessageDao {
    private ConnectionMaker connectionMaker;

    public MessageDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
