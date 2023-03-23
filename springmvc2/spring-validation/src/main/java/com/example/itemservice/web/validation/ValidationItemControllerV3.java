package com.example.itemservice.web.validation;

import com.example.itemservice.domain.item.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/validation/v3/items")
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;

    private static void validateTotalPrice(ItemParam itemParam, Errors errors) {
        if (itemParam.getPrice() != null && itemParam.getQuantity() != null
            && itemParam.getPrice() * itemParam.getQuantity() < 10000) {
            errors.reject("totalPriceMin.item", new Object[]{10000}, null);
        }
    }

    @GetMapping
    public String items(Model model) {
        log.info("ValidationItemControllerV3.items");

        List<Item> items = itemRepository.findAll();
        log.info("items = {}", items);
        model.addAttribute("items", items);

        return "validation/v3/items";
    }

    @GetMapping("/{id}")
    public String itemDetail(Model model, @PathVariable Long id) {
        log.info("ValidationItemControllerV3.itemDetail");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("itemParam") ItemParam item) {
        log.info("ValidationItemControllerV3.addForm");
        return "validation/v3/addForm";
    }

    @PostMapping("/add")
    public String addProcess(@Validated(SaveCheck.class) @ModelAttribute("itemParam") ItemParam itemParam,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("ValidationItemControllerV3.addProcess");

        validateTotalPrice(itemParam, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(itemParam.toItem());
        log.info("savedItem = {}", savedItem);

        redirectAttributes.addAttribute("id", savedItem.getId());
        redirectAttributes.addAttribute("saved", true);

        return "redirect:/validation/v3/items/{id}";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, @PathVariable Long id) {
        log.info("ValidationItemControllerV3.editForm");

        ItemParam itemParam = ItemParam.from(itemRepository.findById(id));
        log.info("item = {}", itemParam);
        model.addAttribute("itemParam", itemParam);

        return "validation/v3/editForm";
    }

    @PostMapping("/{id}/edit")
    public String editProcess(@Validated(UpdateCheck.class) @ModelAttribute("itemParam") ItemParam updateParam,
                              BindingResult bindingResult, @PathVariable Long id) {
        log.info("ValidationItemControllerV3.editProcess");

        if (!id.equals(updateParam.getId())) {
            throw new IllegalArgumentException("Path variable id not equal to request parameter id.");
        }

        validateTotalPrice(updateParam, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "validation/v3/editForm";
        }

        log.info("updateParam = {}", updateParam);

        itemRepository.update(updateParam.getId(), updateParam);

        return "redirect:/validation/v3/items/{id}";
    }
}
