package com.example.itemservice.web.message;

import com.example.itemservice.domain.item.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/message/items")
public class MessageItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        log.info("MessageItemController.items");

        List<Item> items = itemRepository.findAll();
        log.info("items = {}", items);
        model.addAttribute("items", items);

        return "message/items";
    }

    @GetMapping("/{id}")
    public String itemDetail(Model model, @PathVariable Long id) {
        log.info("MessageItemController.itemDetail");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "message/item";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("item") ItemParam item) {
        log.info("MessageItemController.addForm");
        return "message/addForm";
    }

    @PostMapping("/add")
    public String addProcess(@ModelAttribute("item") ItemParam itemParam, RedirectAttributes redirectAttributes) {
        log.info("MessageItemController.addProcess");

        Item savedItem = itemRepository.save(itemParam.toItem());
        log.info("savedItem = {}", savedItem);

        redirectAttributes.addAttribute("id", savedItem.getId());
        redirectAttributes.addAttribute("saved", true);

        return "redirect:/message/items/{id}";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, @PathVariable Long id) {
        log.info("MessageItemController.editForm");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "message/editForm";
    }

    @PostMapping("/{id}/edit")
    public String editProcess(@PathVariable Long id,
                              @RequestParam("id") Long paramId,
                              @ModelAttribute ItemParam updateParam) {
        log.info("MessageItemController.editProcess");
        if (!id.equals(paramId)) {
            throw new IllegalArgumentException("Path variable id not equal to request parameter id.");
        }

        log.info("paramId = {}", paramId);
        log.info("updateParam = {}", updateParam);

        itemRepository.update(paramId, updateParam);

        return "redirect:/message/items/{id}";
    }
}
