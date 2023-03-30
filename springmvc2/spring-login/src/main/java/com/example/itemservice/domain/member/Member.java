package com.example.itemservice.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Member {

    private Long id;

    private String loginId;
    private String password;
    private String name;

    public Member(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }

    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }
}
