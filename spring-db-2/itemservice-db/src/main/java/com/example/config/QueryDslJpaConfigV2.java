package com.example.config;

import com.example.repository.ItemRepository;
import com.example.repository.jpa.JpaItemRepositoryV3;
import com.example.repository.jpa.SpringDataJpaItemRepository;
import com.example.repository.v2.ItemQueryRepository;
import com.example.repository.v2.ItemRepositoryV2;
import com.example.service.ItemService;
import com.example.service.ItemServiceV2;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QueryDslJpaConfigV2 {

    @Bean
    public ItemQueryRepository itemQueryRepository(EntityManager em) {
        return new ItemQueryRepository(em);
    }

    @Bean
    public ItemService itemService(ItemRepositoryV2 itemRepository, ItemQueryRepository itemQueryRepository) {
        return new ItemServiceV2(itemRepository, itemQueryRepository);
    }

    @Bean
    public ItemRepository itemRepository(EntityManager em, SpringDataJpaItemRepository repository) {
        return new JpaItemRepositoryV3(repository, em);
    }

}
