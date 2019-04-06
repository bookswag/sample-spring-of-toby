package com.bookswag.spring.learningtest;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;


public class JUnitTest {
    static Set<JUnitTest> testObject = new HashSet<JUnitTest>();

    @Test
    public void test1() {
        assertThat(testObject, not(hasItem(this)));
        testObject.add(this);
    }

    @Test
    public void test2() {
        assertThat(testObject, not(hasItem(this)));
        testObject.add(this);
    }

    @Test
    public void test3() {
        assertThat(testObject, not(hasItem(this)));
        testObject.add(this);
    }
}
