package study.springfileupload.domain;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRepository {

    private final Map<Long, Item> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Item save(Item item) {
        item.setId(sequence.getAndAdd(1));
        store.put(item.getId(), item);
        return item;
    }

    public Optional<Item> findById(Long id){
        return Optional.ofNullable(store.get(id));
    }

    public List<Item> findAll() {
        return store.values().stream().toList();
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    public void update(Long id, Item updateValue) {
        Item item = store.get(id);
        if (item == null) throw new IllegalStateException("Not exists item");
        item.setName(updateValue.getName());
        item.setAttachFile(updateValue.getAttachFile());
        item.setImageFiles(updateValue.getImageFiles());
    }

    public void delete(Long id) {
        store.remove(id);
    }
}
