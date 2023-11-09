package com.example.config;

import com.example.repository.ItemRepository;
import com.example.repository.memory.MemoryItemRepository;
import com.example.service.ItemService;
import com.example.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfig {

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }

}
