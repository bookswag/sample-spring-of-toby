package com.bookswag.spring.learningtest;

import org.junit.Test;

import java.lang.reflect.Method;

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

        Hello proxiedHello = new HelloUpper(new HelloSimple());
        assertThat(proxiedHello.sayHello(NAME), is("HELLO BOOKSWAG"));
        assertThat(proxiedHello.sayHi(NAME), is("HI BOOKSWAG"));
        assertThat(proxiedHello.sayThankYou(NAME), is("THANK YOU BOOKSWAG"));
    }
}
