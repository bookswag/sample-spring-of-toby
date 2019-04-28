package com.bookswag.spring.database;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;


/**
 * Good things
 * 1. Don't have to create each proxy class (e.g. UserServiceTx, BoardServiceTx, ...)
 * 2. Remove duplicate code for additional function (in this case, transaction)
 *
 * Bad thigns
 * 1. Can't adapt common additional function to a lot of target classes at once
 * 2. Be tough to adapt multi-additional function to a target
* */
@Setter
public class TxProxyFactoryBean implements FactoryBean<Object> {
    private Object target;
    private PlatformTransactionManager transactionManager;
    private String pattern;
    Class<?> serviceInterface;

    @Override
    public Object getObject() throws Exception {
        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(target);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern(pattern);

        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { serviceInterface },
                txHandler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
