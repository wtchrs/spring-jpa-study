package com.example.config;

import com.example.repository.ItemRepository;
import com.example.repository.mybatis.ItemMapper;
import com.example.repository.mybatis.MyBatisItemRepository;
import com.example.service.ItemService;
import com.example.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final ItemMapper itemMapper;

    @Bean
    public ItemRepository itemRepository() {
        return new MyBatisItemRepository(itemMapper);
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

}
