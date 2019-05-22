package com.bookswag.spring.service;

import com.bookswag.spring.dao.UserDao;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import lombok.Setter;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@Service
public class UserServiceImpl implements UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50; // it's only use on Test.
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    private static final String ADMIN_MAIL = "its.bookswag@gmail.com";
    private UserDao userDao;
    private MailSender mailSender;

    @Override
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
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

    void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom(ADMIN_MAIL);
        mailMessage.setSubject("Notice to upgrade");
        mailMessage.setText("Your level upgraded to "+user.getLevel().name());

        mailSender.send(mailMessage);
    }

    @Override
    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    @Override
    public User get(String id)  { return userDao.get(id); }

    @Override
    public List<User> getAll()  { return userDao.getAll(); }

    @Override
    public void deleteAll()     { userDao.deleteAll(); }

    @Override
    public void update(User user) { userDao.update(user); }
}
