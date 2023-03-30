package com.example.itemservice.domain.item;

import com.example.itemservice.web.item.form.ItemUpdateForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {

    private final ItemRepository itemRepository = new ItemRepository();

    @BeforeEach
    void beforeEach() {
        itemRepository.clear();
    }

    @Test
    void save() {
        Item item = new Item("item1", 1000, 10);
        itemRepository.save(item);

        Item savedItem = itemRepository.findById(item.getId());

        assertThat(savedItem).isEqualTo(item);
    }

    @Test
    void findAll() {
        Item item1 = new Item("item1", 1000, 10);
        Item item2 = new Item("item2", 1000, 10);
        Item item3 = new Item("item3", 1000, 10);
        Item item4 = new Item("item4", 1000, 10);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);

        List<Item> items = itemRepository.findAll();

        assertThat(items).containsExactly(item1, item2, item3, item4);
    }

    @Test
    void update() {
        Item item = new Item("item1", 1000, 10);
        itemRepository.save(item);
        ItemUpdateForm form = new ItemUpdateForm("Changed Name", 10, 5);
        itemRepository.update(item.getId(), form.toItem());

        Item savedItem = itemRepository.findById(item.getId());

        assertThat(savedItem.getItemName()).isEqualTo(form.getItemName());
        assertThat(savedItem.getPrice()).isEqualTo(form.getPrice());
        assertThat(savedItem.getQuantity()).isEqualTo(form.getQuantity());
    }
}