package com.example.repository.jpa;

import com.example.domain.Item;
import com.example.repository.ItemRepository;
import com.example.repository.ItemSearchCond;
import com.example.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JpaItemRepository implements ItemRepository {

    /**
     * Spring Boot automates the configuration of {@link DataSource}, {@link EntityManagerFactory},
     * {@link JpaTransactionManager}, and so on.
     */
    private final EntityManager em;

    public JpaItemRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Transactional
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = em.find(Item.class, itemId);

        if (item == null) {
            throw new NullPointerException("Item does not exist");
        }

        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(em.find(Item.class, id));
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String jpql = "select i from Item i";

        if (!ObjectUtils.isEmpty(cond.getItemName()) || !ObjectUtils.isEmpty(cond.getMaxPrice())) {
            jpql += " where ";
        }

        boolean hasPrevCond = false;

        if (!ObjectUtils.isEmpty(cond.getItemName())) {
            jpql += "i.itemName like concat('%', :itemName, '%')";
            hasPrevCond = true;
        }

        if (!ObjectUtils.isEmpty(cond.getMaxPrice())) {
            if (hasPrevCond) {
                jpql += " and ";
            }
            jpql += "i.price <= :maxPrice";
        }

        log.info("jpql = {}", jpql);

        TypedQuery<Item> query = em.createQuery(jpql, Item.class);

        if (!ObjectUtils.isEmpty(cond.getItemName())) {
            query.setParameter("itemName", cond.getItemName());
        }

        if (!ObjectUtils.isEmpty(cond.getMaxPrice())) {
            query.setParameter("maxPrice", cond.getMaxPrice());
        }

        return query.getResultList();
    }
}
