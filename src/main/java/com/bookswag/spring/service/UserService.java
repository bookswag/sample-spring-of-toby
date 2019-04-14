package com.bookswag.spring.service;

import com.bookswag.spring.dao.UserDao;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@Service
public class UserService {
    private UserDao userDao;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for(User user : users) {
            Boolean changed = null; // Using Reference type is not good idea, as flag specially
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            }
            else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            }
            else if (user.getLevel() == Level.GOLD) {
                changed = false;
            }
            else {
                changed = false;
            }

            if (changed) {
                userDao.update(user);
            }
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
