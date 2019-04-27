package com.bookswag.spring.common;

public class UnsupportedMethodException extends RuntimeException {
    public UnsupportedMethodException() {
        super("Not supported method");
    }
}
