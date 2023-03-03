package project.springbootexample.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springbootexample.domain.Member;
import project.springbootexample.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long join(Member member) {
        validateDuplicate(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicate(Member member) {
        memberRepository
                .findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("Already exist duplicate name");
                });
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
