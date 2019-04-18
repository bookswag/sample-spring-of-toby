package com.bookswag.spring.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String id;
    private String name;
    private String password;
    private String email;

    private Level level;
    private int login;
    private int recommend;

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "can't upgrade");
        }
        else {
            this.level = nextLevel;
        }
    }
}
