package com.bookswag.spring.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    String id;
    String name;
    String password;
}
