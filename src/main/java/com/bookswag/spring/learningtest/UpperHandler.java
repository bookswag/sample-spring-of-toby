package com.bookswag.spring.learningtest;

import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@AllArgsConstructor
public class UpperHandler implements InvocationHandler {
    private Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        return ret instanceof String && method.getName().startsWith("say") ? ((String)ret).toUpperCase() : ret;
    }
}
