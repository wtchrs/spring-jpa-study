package com.example.itemservice.web.member.form;

import com.example.itemservice.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

    @NotBlank
    private String loginId;
    @NotBlank
    private String name;
    @NotBlank
    private String password;

    public RegisterForm(String loginId, String name, String password) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
    }

    public Member toMember() {
        return new Member(loginId, password, name);
    }
}
