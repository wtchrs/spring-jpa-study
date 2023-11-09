package com.example.config;

import com.example.repository.ItemRepository;
import com.example.repository.jpa.JpaItemRepository;
import com.example.service.ItemService;
import com.example.service.ItemServiceV1;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JpaConfig {

    private final EntityManager em;

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepository(em);
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

}
