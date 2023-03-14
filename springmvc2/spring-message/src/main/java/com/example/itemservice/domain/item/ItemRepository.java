package com.example.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    public Item save(Item item) {
        item.setId(sequence.addAndGet(1));
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.getOrDefault(id, null);
    }

    public List<Item> findAll() {
        return store.values().stream().toList();
    }

    public void update(Long id, ItemParam itemUpdate){
        Item item = store.get(id);
        item.setItemName(itemUpdate.getItemName());
        item.setPrice(itemUpdate.getPrice());
        item.setQuantity(itemUpdate.getQuantity());
    }

    public void delete(Long id) {
        store.remove(id);
    }

    public void clear() {
        store.clear();
    }
}
