package com.bookswag.spring.dao;

import com.bookswag.spring.database.ConnectionMaker;
import com.bookswag.spring.database.NConnectionMaker;
import com.bookswag.spring.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;


/**
 * progress of workflow on JDBC
 * 1. Get 'Connection' for connection between APP with DB
 * 2. Make up 'Statement(or, PreparedStatement)' has SQL
 * 3. Execute that 'Statement'
 * 4. in view case, Put the result of Query execution to 'ResultSet' & Move to Object (in this case, User)
 * 5. Must Close all resource such as Connection, Statement, ResultSet at last time
 * 6. Handle exception from JDBC API, or Declare 'throws' to method to do throw out of it
 */
public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        User newUser = new User();
        newUser.setId("spring");
        newUser.setName("bookswag");
        newUser.setPassword("1234");

        dao.add(newUser);

        System.out.println(newUser.getId() + " is registered.");


        User dbUser = dao.get(newUser.getId());

        if (!newUser.getName().equals(dbUser.getName())) {
            System.out.println("Test fail (name)");
        } else if (!newUser.getPassword().equals(dbUser.getPassword())) {
            System.out.println("Test fail (password");
        } else {
            System.out.println("Test success");
        }
    }
}