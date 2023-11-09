package com.example.config;

import com.example.repository.ItemRepository;
import com.example.repository.jpa.JpaItemRepositoryV3;
import com.example.repository.jpa.SpringDataJpaItemRepository;
import com.example.service.ItemService;
import com.example.service.ItemServiceV1;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QueryDslJpaConfig {

    private final SpringDataJpaItemRepository repository;

    private final EntityManager em;

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(repository, em);
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

}
