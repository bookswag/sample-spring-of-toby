package com.bookswag.spring.database;

import com.bookswag.spring.domain.User;
import com.bookswag.spring.service.UserService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Setter
@Deprecated
public class UserServiceTx implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTx.class);
    private UserService userService;
    private PlatformTransactionManager transactionManager;

    @Override
    public void add(User user){
        userService.add(user);
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            userService.upgradeLevels();
            this.transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            this.transactionManager.rollback(transactionStatus);
            LOGGER.error("Error on transaction : {}", e);
            throw e;
        }
    }

    @Override
    public User get(String id) { throw new UnsupportedOperationException();	}
    @Override
    public List<User> getAll() { throw new UnsupportedOperationException();	}
    @Override
    public void deleteAll() { throw new UnsupportedOperationException();	}
    @Override
    public void update(User user) { throw new UnsupportedOperationException();	}
}
