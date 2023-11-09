package com.example.config;

import com.example.repository.ItemRepository;
import com.example.repository.jpa.JpaItemRepositoryV2;
import com.example.repository.jpa.SpringDataJpaItemRepository;
import com.example.service.ItemService;
import com.example.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {

    private final SpringDataJpaItemRepository repository;

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV2(repository);
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

}
