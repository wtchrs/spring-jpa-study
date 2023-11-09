package com.example.config;

import com.example.repository.ItemRepository;
import com.example.repository.jdbctemplate.JdbcTemplateItemRepositoryV1;
import com.example.service.ItemService;
import com.example.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateConfigV1 {

    private final DataSource ds;

    @Bean
    public ItemRepository itemRepository() {
        return new JdbcTemplateItemRepositoryV1(ds);
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

}
