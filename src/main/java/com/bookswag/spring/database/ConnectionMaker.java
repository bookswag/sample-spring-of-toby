package com.bookswag.spring.database;

import java.sql.Connection;
import java.sql.SQLException;

@Deprecated
public interface ConnectionMaker {
    Connection makeConnection() throws ClassNotFoundException, SQLException;
}
