package com.example.itemservice.web.validation;

import com.example.itemservice.domain.item.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/validation/v1/items")
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    private static Map<String, String> getValidationErrors(ItemParam itemParam) {
        Map<String, String> errors = new HashMap<>();
        if (!StringUtils.hasText(itemParam.getItemName())) {
            errors.put("itemName", "required.item.itemName");
        }
        if (itemParam.getPrice() == null || itemParam.getPrice() < 1000 || itemParam.getPrice() > 1000000) {
            errors.put("price", "range.item.price");
        }
        if (itemParam.getQuantity() == null || itemParam.getQuantity() < 1 || itemParam.getQuantity() > 9999) {
            errors.put("quantity", "range.item.quantity");
        }
        if (itemParam.getPrice() != null && itemParam.getQuantity() != null
            && itemParam.getPrice() * itemParam.getQuantity() < 10000) {
            errors.put("global", "totalPriceMin");
        }
        return errors;
    }

    @GetMapping
    public String items(Model model) {
        log.info("ValidationItemControllerV1.items");

        List<Item> items = itemRepository.findAll();
        log.info("items = {}", items);
        model.addAttribute("items", items);

        return "validation/v1/items";
    }

    @GetMapping("/{id}")
    public String itemDetail(Model model, @PathVariable Long id) {
        log.info("ValidationItemControllerV1.itemDetail");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("item") ItemParam item) {
        log.info("ValidationItemControllerV1.addForm");
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addProcess(@ModelAttribute("item") ItemParam itemParam,
                             Model model, RedirectAttributes redirectAttributes) {
        log.info("ValidationItemControllerV1.addProcess");

        Map<String, String> errors = getValidationErrors(itemParam);

        if (!errors.isEmpty()) {
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }

        Item savedItem = itemRepository.save(itemParam.toItem());
        log.info("savedItem = {}", savedItem);

        redirectAttributes.addAttribute("id", savedItem.getId());
        redirectAttributes.addAttribute("saved", true);

        return "redirect:/validation/v1/items/{id}";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, @PathVariable Long id) {
        log.info("ValidationItemControllerV1.editForm");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "validation/v1/editForm";
    }

    @PostMapping("/{id}/edit")
    public String editProcess(@PathVariable Long id, @ModelAttribute("item") ItemParam updateParam, Model model) {
        log.info("ValidationItemControllerV1.editProcess");
        if (!id.equals(updateParam.getId())) {
            throw new IllegalArgumentException("Path variable id not equal to request parameter id.");
        }

        Map<String, String> errors = getValidationErrors(updateParam);

        if (!errors.isEmpty()) {
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/editForm";
        }

        log.info("updateParam = {}", updateParam);

        itemRepository.update(updateParam.getId(), updateParam);

        return "redirect:/validation/v1/items/{id}";
    }
}
