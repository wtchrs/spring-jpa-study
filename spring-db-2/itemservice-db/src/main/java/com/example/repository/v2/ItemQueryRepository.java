package com.example.repository.v2;

import com.example.domain.Item;
import com.example.repository.ItemSearchCond;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.domain.QItem.*;
import static com.example.repository.v2.BooleanExpressionUtils.*;

@Repository
public class ItemQueryRepository {

    private final JPAQueryFactory query;

    public ItemQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond) {
        return query.select(item)
                .from(item)
                .where(
                        itemNameLike(cond.getItemName()),
                        priceLessThanEq(cond.getMaxPrice())
                )
                .fetch();
    }

}
