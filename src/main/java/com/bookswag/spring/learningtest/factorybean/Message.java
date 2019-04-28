package com.bookswag.spring.learningtest.factorybean;

import lombok.Getter;

@Getter
public class Message {
    String text;

    private Message(String text) {
        this.text = text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }
}
