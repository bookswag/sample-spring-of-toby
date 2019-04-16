package com.bookswag.spring.service;

import com.bookswag.spring.domain.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class TestUserService extends UserService {
    private String id;

    protected void upgradeLevel(Connection c, User user) throws SQLException {
        if (StringUtils.equals(user.getId(), this.id)) {
            throw new TestUserServiceException();
        }
        super.upgradeLevel(c, user);
    }
}
