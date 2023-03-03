package project.springbootexample.repository;

import project.springbootexample.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> store = new HashMap<>();
    private Long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(sequence, member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream().filter(member -> member.getName().equals(name)).findAny();
    }

    @Override
    public List<Member> findAll() {
        return new LinkedList<>(store.values());
    }

    public void clear(){
        store.clear();
        sequence = 0L;
    }
}
