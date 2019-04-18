package com.bookswag.spring.service;

import com.bookswag.spring.dao.UserDao;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

@Setter
@Service
public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50; // it's only use on Test.
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    private static final String ADMIN_MAIL = "its.bookswag@gmail.com";
    private UserDao userDao;
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() throws Exception {
        TransactionStatus transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            this.transactionManager.rollback(transactionStatus);
            throw e;
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
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.bookswag.org");
        Session s = Session.getInstance(props, null);

        MimeMessage message = new MimeMessage(s);
        try {
            message.setFrom(new InternetAddress(ADMIN_MAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Notice to upgrade");
            message.setText("Your level upgrades to "+user.getLevel().name());
            Transport.send(message);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
//        catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
