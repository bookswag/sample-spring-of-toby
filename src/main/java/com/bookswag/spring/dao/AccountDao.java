package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;

/**
 * Dummy class for big DaoFactory
 */
@Deprecated
public class AccountDao {
    private ConnectionMaker connectionMaker;

    public AccountDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
