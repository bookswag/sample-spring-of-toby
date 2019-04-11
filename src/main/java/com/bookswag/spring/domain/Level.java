package com.bookswag.spring.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Level {
    BASIC(1), SILVER(2), GOLD(3);

    private final int value;

    public int intValue() {
        return value;
    }

    public static Level valueOf(int value) {
        switch(value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknown value : " + value);
        }
    }
}
