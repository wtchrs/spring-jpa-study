package com.example.itemservice.web.item;

import com.example.itemservice.domain.item.*;
import com.example.itemservice.web.item.form.ItemSaveForm;
import com.example.itemservice.web.item.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        log.info("ItemController.items");

        List<Item> items = itemRepository.findAll();
        log.info("items = {}", items);
        model.addAttribute("items", items);

        return "items/items";
    }

    @GetMapping("/{id}")
    public String itemDetail(Model model, @PathVariable Long id) {
        log.info("ItemController.itemDetail");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "items/item";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("item") ItemSaveForm form) {
        log.info("ItemController.addForm");
        return "items/addForm";
    }

    @PostMapping("/add")
    public String addProcess(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.info("ItemController.addProcess");

        if (form.getPrice() != null && form.getQuantity() != null && form.getPrice() * form.getQuantity() < 10000) {
            bindingResult.reject("totalPriceMin.item", new Object[]{10000}, null);
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "items/addForm";
        }

        Item savedItem = itemRepository.save(form.toItem());
        log.info("savedItem = {}", savedItem);

        redirectAttributes.addAttribute("id", savedItem.getId());
        redirectAttributes.addAttribute("saved", true);

        return "redirect:/items/{id}";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, @PathVariable Long id) {
        log.info("ItemController.editForm");

        ItemUpdateForm form = ItemUpdateForm.from(itemRepository.findById(id));
        log.info("itemUpdateRequest = {}", form);
        model.addAttribute("item", form);

        return "items/editForm";
    }

    @PostMapping("/{id}/edit")
    public String editProcess(@Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult,
                              @PathVariable Long id) {
        log.info("ItemController.editProcess");

        if (!id.equals(form.getId())) {
            throw new IllegalArgumentException("Path variable id not equal to request parameter id.");
        }

        if (form.getPrice() != null && form.getQuantity() != null && form.getPrice() * form.getQuantity() < 10000) {
            bindingResult.reject("totalPriceMin.item", new Object[]{10000}, null);
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "items/editForm";
        }

        log.info("form = {}", form);

        itemRepository.update(form.getId(), form.toItem());

        return "redirect:/items/{id}";
    }
}
