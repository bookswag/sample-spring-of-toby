package com.bookswag.spring.service;

import com.bookswag.spring.domain.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public class TestUserService extends UserService {
    private String id;

    protected void upgradeLevel(User user) {
        if (StringUtils.equals(user.getId(),this.id)) {
            throw new TestUserServiceException();
        }
        super.upgradeLevel(user);
    }
}
