package com.bookswag.spring.database;

import lombok.Setter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Deprecated
@Setter
public class TransactionHandler implements InvocationHandler {
    private Object target;
    private PlatformTransactionManager transactionManager;
    private String pattern;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.getName().startsWith(pattern) ? invokeInTransaction(method, args) : method.invoke(target, args);
    }

    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = method.invoke(target, args);
            this.transactionManager.commit(transactionStatus);
            return ret;
        } catch (InvocationTargetException e) {
            this.transactionManager.rollback(transactionStatus);
            throw e.getTargetException();
        }
    }
}
