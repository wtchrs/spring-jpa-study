package com.example.repository.memory;

import com.example.domain.Item;
import com.example.repository.ItemRepository;
import com.example.repository.ItemSearchCond;
import com.example.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class MemoryItemRepository implements ItemRepository {

    private static final Map<Long, Item> store = new ConcurrentHashMap<>(); //static
    private static final AtomicLong sequence = new AtomicLong(); //static

    @Override
    public Item save(Item item) {
        item.setId(sequence.incrementAndGet());
        store.put(item.getId(), item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        return store.values().stream()
                .filter(item -> ObjectUtils.isEmpty(itemName) || item.getItemName().contains(itemName))
                .filter(item -> maxPrice == null || item.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public void clearStore() {
        store.clear();
    }

}
