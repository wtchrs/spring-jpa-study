package com.example.itemservice;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.ItemRepository;
import com.example.itemservice.domain.member.Member;
import com.example.itemservice.domain.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DemoDataInitializer {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    /**
     * Initialize {@link ItemRepository} with dummy data for demo.
     */
    @PostConstruct
    public void initData() {
        log.info("DemoDataInitializer.initData");

        itemRepository.save(new Item("Item A", 10000, 100));
        itemRepository.save(new Item("Item B", 15000, 50));
        List<Item> savedItems = itemRepository.findAll();
        log.info("savedItems = {}", savedItems);

        memberRepository.save(new Member("test", "test!", "Tester"));
        List<Member> savedMembers = memberRepository.findAll();
        log.info("savedMembers = {}", savedMembers);
    }
}
