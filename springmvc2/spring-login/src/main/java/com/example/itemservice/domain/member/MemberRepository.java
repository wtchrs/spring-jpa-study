package com.example.itemservice.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class MemberRepository {

    private static final Map<Long, Member> store = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    public Member save(Member member) {
        member.setId(sequence.addAndGet(1));
        store.put(member.getId(), member);
        return member;
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<Member> findByLoginId(String loginId) {
        return store.values().stream().filter(member -> member.getLoginId().equals(loginId)).findAny();
    }

    public List<Member> findAll() {
        return store.values().stream().toList();
    }

    public void update(Long id, Member update) {
        Member member = store.get(id);
        if (member == null) throw new IllegalStateException();
        member.setLoginId(update.getLoginId());
        member.setName(update.getName());
        member.setPassword(update.getPassword());
    }

    public void delete(Long id) {
        store.remove(id);
    }

    public void clear() {
        store.clear();
    }
}
