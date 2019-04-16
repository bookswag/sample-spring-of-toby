package com.bookswag.spring.service;

import com.bookswag.spring.dao.UserDao;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Setter
@Service
public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50; // it's only use on Test.
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    private UserDao userDao;

    public void upgradeLevels(Connection c) throws SQLException {
        List<User> users = userDao.getAll(c);
        for(User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(c, user);
            }
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch(currentLevel) {
            case BASIC: return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
            case SILVER: return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
            case GOLD: return false;
            default:
                throw new IllegalArgumentException("Unknown level : "+ currentLevel);
        }
    }

    void upgradeLevel(Connection c, User user) throws SQLException {
        user.upgradeLevel();
        userDao.update(c, user);
    }

    public void add(Connection c, User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(c, user);
    }
}
