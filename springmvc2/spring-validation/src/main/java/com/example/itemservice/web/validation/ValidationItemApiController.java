package com.example.itemservice.web.validation;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.ItemRepository;
import com.example.itemservice.web.validation.form.ItemSaveForm;
import com.example.itemservice.web.validation.response.SaveResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
@RequiredArgsConstructor
public class ValidationItemApiController {

    private final ItemRepository itemRepository;

    @PostMapping("/add")
    public Object addItem(@Validated @RequestBody ItemSaveForm form, BindingResult bindingResult) {
        log.info("ValidationItemApiController.addItem");

        if (form.getPrice() != null && form.getQuantity() != null && form.getPrice() * form.getQuantity() < 10000) {
            bindingResult.reject("totalPriceMin.item", new Object[]{10000}, null);
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return bindingResult.getAllErrors();
        }

        Item item = itemRepository.save(form.toItem());
        log.info("item = {}", item);
        return new SaveResult(item.getId());
    }
}
