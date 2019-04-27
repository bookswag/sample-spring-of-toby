package com.bookswag.spring.service;

import com.bookswag.spring.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
