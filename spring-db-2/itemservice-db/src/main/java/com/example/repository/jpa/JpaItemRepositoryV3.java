package com.example.repository.jpa;

import com.example.domain.Item;
import com.example.repository.ItemRepository;
import com.example.repository.ItemSearchCond;
import com.example.repository.ItemUpdateDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.example.domain.QItem.*;

@Slf4j
@Repository
public class JpaItemRepositoryV3 implements ItemRepository {

    private final SpringDataJpaItemRepository repository;

    private final JPAQueryFactory queryFactory;

    public JpaItemRepositoryV3(SpringDataJpaItemRepository repository, EntityManager em) {
        this.repository = repository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    @Transactional
    public Item save(Item item) {
        return repository.save(item);
    }

    @Override
    @Transactional
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = repository.findById(itemId).orElseThrow();
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        return queryFactory.select(item)
                .from(item)
                .where(itemNameLike(itemName), priceLessThanEq(maxPrice))
                .fetch();
    }

    private static BooleanExpression itemNameLike(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private static BooleanExpression priceLessThanEq(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }

    public List<Item> findAllOld(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        BooleanBuilder condition = new BooleanBuilder();

        if (StringUtils.hasText(itemName)) {
            condition.and(itemNameLike(itemName));
        }

        if (maxPrice != null) {
            condition.and(item.price.loe(maxPrice));
        }

        log.info("condition = {}", condition);

        return queryFactory.select(item)
                .from(item)
                .where(condition)
                .fetch();
    }

}
