package com.bookswag.spring.learningtest.pointcut;

public class Target implements TargetInterface{
    @Override
    public void hello() {}
    @Override
    public void hello(String a) {}
    @Override
    public int minus(int a, int b) throws RuntimeException { return 0; }
    @Override
    public int plus(int a, int b) { return 1; }
    public void method() {}
}
