package com.bookswag.spring.database;

import java.sql.Connection;
import java.sql.SQLException;

@Deprecated
public class CountingConnectionMaker implements ConnectionMaker {
    int count = 0;
    private ConnectionMaker actualConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker actualConnectionMaker) {
        this.actualConnectionMaker = actualConnectionMaker;
    }

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        this.count++;
        return actualConnectionMaker.makeConnection();
    }

    public int getCount() {
        return this.count;
    }
}
