package com.example.itemservice.web.basic;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.ItemRepository;
import com.example.itemservice.domain.item.ItemUpdateParam;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

    private final ItemRepository itemRepository;

    /**
     * Initialize {@link ItemRepository} with dummy data for test.
     */
    @PostConstruct
    public void initData() {
        log.info("BasicItemController.initData");

        itemRepository.save(new Item("Item A", 10000, 100));
        itemRepository.save(new Item("Item B", 15000, 50));

        List<Item> savedItems = itemRepository.findAll();
        log.info("savedItems = {}", savedItems);
    }

    @GetMapping
    public String items(Model model) {
        log.info("BasicItemController.items");

        List<Item> items = itemRepository.findAll();
        log.info("items = {}", items);
        model.addAttribute("items", items);

        return "basic/items";
    }

    @GetMapping("/{id}")
    public String itemDetail(Model model, @PathVariable Long id) {
        log.info("BasicItemController.itemDetail");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        log.info("BasicItemController.addForm");
        return "basic/addForm";
    }

    @PostMapping("/add")
    public String addProcess(@ModelAttribute ItemUpdateParam updateParam, RedirectAttributes redirectAttributes) {
        log.info("BasicItemController.addProcess");

        Item savedItem = itemRepository.save(updateParam.toItem());
        log.info("savedItem = {}", savedItem);

        redirectAttributes.addAttribute("id", savedItem.getId());
        redirectAttributes.addAttribute("saved", true);

        return "redirect:/basic/items/{id}";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, @PathVariable Long id) {
        log.info("BasicItemController.editForm");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{id}/edit")
    public String editProcess(@PathVariable Long id,
                              @RequestParam("id") Long paramId,
                              @ModelAttribute ItemUpdateParam updateParam) {
        log.info("BasicItemController.editProcess");
        if (!id.equals(paramId)) {
            throw new IllegalArgumentException("Path variable id not equal to request parameter id.");
        }

        log.info("paramId = {}", paramId);
        log.info("updateParam = {}", updateParam);

        itemRepository.update(paramId, updateParam);

        return "redirect:/basic/items/{id}";
    }
}
