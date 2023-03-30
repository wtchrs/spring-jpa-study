package com.example.itemservice.domain.login;

import com.example.itemservice.domain.member.Member;
import com.example.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Optional<Member> login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId).filter(member -> member.getPassword().equals(password));
    }
}
