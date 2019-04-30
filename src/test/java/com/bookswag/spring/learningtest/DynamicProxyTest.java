package com.bookswag.spring.learningtest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DynamicProxyTest {
    private static final String NAME = "Bookswag";

    @Test
    public void reflectionTest() throws Exception {
        String name = "Spring";

        assertThat(name.length(), is(6));

        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer)lengthMethod.invoke(name), is(6));

        assertThat(name.charAt(0), is('S'));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character)charAtMethod.invoke(name, 0), is('S'));
    }

    @Test
    public void simpleProxy() {
        Hello hello = new HelloSimple();
        assertThat(hello.sayHello(NAME), is("Hello Bookswag"));
        assertThat(hello.sayHi(NAME), is("Hi Bookswag"));
        assertThat(hello.sayThankYou(NAME), is("Thank you Bookswag"));

        Hello proxiedHello = (Hello) Proxy.newProxyInstance( // safety casting. 'cause target of UpperHandler is Hello interface
          getClass().getClassLoader(), // used for dynamic proxy class loading
          new Class[] { Hello.class }, // target interface
          new UpperHandler(new HelloSimple()) // has decorate code to do upper as additional function
        );
        assertThat(proxiedHello.sayHello(NAME), is("HELLO BOOKSWAG"));
        assertThat(proxiedHello.sayHi(NAME), is("HI BOOKSWAG"));
        assertThat(proxiedHello.sayThankYou(NAME), is("THANK YOU BOOKSWAG"));
    }

    @Test
    public void springProxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloSimple());
        proxyFactoryBean.addAdvice((MethodInterceptor) invocation -> {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        });

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();
        assertThat(proxiedHello.sayHello(NAME), is("HELLO BOOKSWAG"));
        assertThat(proxiedHello.sayHi(NAME), is("HI BOOKSWAG"));
        assertThat(proxiedHello.sayThankYou(NAME), is("THANK YOU BOOKSWAG"));
    }
}
