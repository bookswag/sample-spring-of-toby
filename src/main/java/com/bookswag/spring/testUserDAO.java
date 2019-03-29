package com.bookswag.spring;

import com.bookswag.spring.dao.UserDao;
import com.bookswag.spring.domain.User;

import java.sql.SQLException;

public class testUserDAO {
    /**
     * progress of workflow on JDBC
     * 1. Get 'Connection' for connection between APP with DB
     * 2. Make up 'Statement(or, PreparedStatement)' has SQL
     * 3. Execute that 'Statement'
     * 4. in view case, Put the result of Query execution to 'ResultSet' & Move to Object (in this case, User)
     * 5. Must Close all resource such as Connection, Statement, ResultSet at last time
     * 6. Handle exception from JDBC API, or Declare 'throws' to method to do throw out of it
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserDao dao = new UserDao();

        User newUser = new User();
        newUser.setId("spring");
        newUser.setName("bookswag");
        newUser.setPassword("1234");

        dao.add(newUser);

        System.out.println(newUser.getId() + " is registered.");

        User dbUser = dao.get(newUser.getId());
        System.out.println(dbUser.getName());
    }
}
