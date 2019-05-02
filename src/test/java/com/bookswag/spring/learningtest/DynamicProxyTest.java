package com.bookswag.spring.learningtest;

import com.bookswag.spring.learningtest.dynamicproxy.Hello;
import com.bookswag.spring.learningtest.dynamicproxy.HelloSimple;
import com.bookswag.spring.learningtest.dynamicproxy.UpperHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

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

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloSimple());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        proxyFactoryBean.addAdvisor(
                new DefaultPointcutAdvisor(
                        pointcut, (MethodInterceptor) invocation -> {
                            String ret = (String) invocation.proceed();
                            return ret.toUpperCase();
                        }));

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();
        assertThat(proxiedHello.sayHello(NAME), is("HELLO BOOKSWAG"));
        assertThat(proxiedHello.sayHi(NAME), is("HI BOOKSWAG"));
        assertThat(proxiedHello.sayThankYou(NAME), is("Thank you Bookswag"));
    }

    @Test
    public void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return (clazz) -> clazz.getSimpleName().startsWith("HelloS");
            }
        };
        
        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloSimple(), classMethodPointcut, true);

        class HelloWorld extends HelloSimple {}
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloBookswag extends HelloSimple {}
        checkAdviced(new HelloBookswag(), classMethodPointcut, false);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(target);
        proxyFactoryBean.addAdvisor(
                new DefaultPointcutAdvisor(
                    pointcut, (MethodInterceptor) invocation -> {
                        String ret = (String) invocation.proceed();
                        return ret.toUpperCase();
                }));

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello(NAME), is("HELLO BOOKSWAG"));
            assertThat(proxiedHello.sayHi(NAME), is("HI BOOKSWAG"));
            assertThat(proxiedHello.sayThankYou(NAME), is("Thank you Bookswag"));
        } else {
            assertThat(proxiedHello.sayHello(NAME), is("Hello Bookswag"));
            assertThat(proxiedHello.sayHi(NAME), is("Hi Bookswag"));
            assertThat(proxiedHello.sayThankYou(NAME), is("Thank you Bookswag"));
        }


    }
}
