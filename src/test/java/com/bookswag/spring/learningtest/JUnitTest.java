package com.bookswag.spring.learningtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.either;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test/junit.xml")
public class JUnitTest {
    @Autowired
    ApplicationContext context;

    static Set<JUnitTest> testObject = new HashSet<JUnitTest>();
    static ApplicationContext staticContext = null;

    @Test
    public void test1() {
        assertThat(testObject, not(hasItem(this)));
        testObject.add(this);

        assertThat(staticContext == null || staticContext == this.context, is(true));
        staticContext = this.context;
    }

    @Test
    public void test2() {
        assertThat(testObject, not(hasItem(this)));
        testObject.add(this);

        assertTrue(staticContext == null || staticContext == this.context);
        staticContext = this.context;
    }

    @Test
    public void test3() {
        assertThat(testObject, not(hasItem(this)));
        testObject.add(this);

        assertThat(staticContext, either(is(nullValue())).or(is(this.context)));
        staticContext = this.context;
    }
}
