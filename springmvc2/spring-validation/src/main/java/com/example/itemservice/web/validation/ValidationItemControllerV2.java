package com.example.itemservice.web.validation;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.ItemParam;
import com.example.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/validation/v2/items")
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final MessageSource messageSource;
    private final ItemParamValidator itemParamValidator;

    @InitBinder("item")
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemParamValidator);
    }

    private void validateItemParamV1(ItemParam itemParam, BindingResult bindingResult, Locale locale) {
        log.info("ValidationItemControllerV2.validateItemParamV1");

        if (!StringUtils.hasText(itemParam.getItemName())) {
            String errorMessage = messageSource.getMessage("required.item.itemName", null, locale);
            bindingResult.addError(new FieldError("item", "itemName", errorMessage));
        }
        if (itemParam.getPrice() == null || itemParam.getPrice() < 1000 || itemParam.getPrice() > 1000000) {
            String errorMessage = messageSource.getMessage("range.item.price", null, locale);
            bindingResult.addError(new FieldError("item", "price", errorMessage));
        }
        if (itemParam.getQuantity() == null || itemParam.getQuantity() < 1 || itemParam.getQuantity() > 9999) {
            String errorMessage = messageSource.getMessage("range.item.quantity", null, locale);
            bindingResult.addError(new FieldError("item", "quantity", errorMessage));
        }
        if (itemParam.getPrice() != null && itemParam.getQuantity() != null
            && itemParam.getPrice() * itemParam.getQuantity() < 10000) {
            String errorMessage = messageSource.getMessage("totalPriceMin", null, locale);
            bindingResult.addError(new ObjectError("item", errorMessage));
        }
    }

    private void validateItemParamV2(ItemParam itemParam, BindingResult bindingResult, Locale locale) {
        log.info("ValidationItemControllerV2.validateItemParamV2");

        if (!StringUtils.hasText(itemParam.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", itemParam.getItemName(), false,
                                                  new String[]{"required.item.itemName"}, null, null));
        }
        if (itemParam.getPrice() == null || itemParam.getPrice() < 1000 || itemParam.getPrice() > 1000000) {
            bindingResult.addError(
                    new FieldError("item", "price", itemParam.getPrice(), false, new String[]{"range.item.price"},
                                   new Object[]{"1,000", "1,000,000"}, null));
        }
        if (itemParam.getQuantity() == null || itemParam.getQuantity() < 1 || itemParam.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", itemParam.getQuantity(), false,
                                                  new String[]{"range.item.quantity"}, new Object[]{"1", "9,999"},
                                                  null));
        }
        if (itemParam.getPrice() != null && itemParam.getQuantity() != null
            && itemParam.getPrice() * itemParam.getQuantity() < 10000) {
            bindingResult.addError(
                    new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{"10,000"}, null));
        }
    }

    private void validateItemParamV3(BindingResult bindingResult) {
        log.info("ValidationItemControllerV2.validateItemParamV3");
        ItemParam itemParam = (ItemParam) bindingResult.getTarget();
        if (itemParam == null) throw new NullPointerException("BindingResult::getTarget() returned null.");

        if (!StringUtils.hasText(itemParam.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }
        if ((itemParam.getPrice() == null || itemParam.getPrice() < 1000 || itemParam.getPrice() > 1000000)
            && bindingResult.getFieldError("price") == null) {
            bindingResult.rejectValue("price", "range", new Object[]{"1,000", "1,000,000"}, null);
        }
        if ((itemParam.getQuantity() == null || itemParam.getQuantity() < 1 || itemParam.getQuantity() > 9999)
            && bindingResult.getFieldError("quantity") == null) {
            bindingResult.rejectValue("quantity", "range", new Object[]{"1", "9,999"}, null);
        }
        if (itemParam.getPrice() != null && itemParam.getQuantity() != null
            && itemParam.getPrice() * itemParam.getQuantity() < 10000) {
            bindingResult.reject("totalPriceMin", new Object[]{"10,000"}, null);
        }
    }

    @GetMapping
    public String items(Model model) {
        log.info("ValidationItemControllerV2.items");

        List<Item> items = itemRepository.findAll();
        log.info("items = {}", items);
        model.addAttribute("items", items);

        return "validation/v2/items";
    }

    @GetMapping("/{id}")
    public String itemDetail(Model model, @PathVariable Long id) {
        log.info("ValidationItemControllerV2.itemDetail");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("itemParam") ItemParam item) {
        log.info("ValidationItemControllerV2.addForm");
        return "validation/v2/addForm";
    }

    @PostMapping("/add")
    public String addProcess(@Validated @ModelAttribute("itemParam") ItemParam itemParam, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.info("ValidationItemControllerV2.addProcess");

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(itemParam.toItem());
        log.info("savedItem = {}", savedItem);

        redirectAttributes.addAttribute("id", savedItem.getId());
        redirectAttributes.addAttribute("saved", true);

        return "redirect:/validation/v2/items/{id}";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, @PathVariable Long id) {
        log.info("ValidationItemControllerV2.editForm");

        ItemParam itemParam = ItemParam.from(itemRepository.findById(id));
        log.info("item = {}", itemParam);
        model.addAttribute("itemParam", itemParam);

        return "validation/v2/editForm";
    }

    @PostMapping("/{id}/edit")
    public String editProcess(@Validated @ModelAttribute("itemParam") ItemParam updateParam,
                              BindingResult bindingResult, @PathVariable Long id) {
        log.info("ValidationItemControllerV2.editProcess");

        if (!id.equals(updateParam.getId())) {
            throw new IllegalArgumentException("Path variable id not equal to request parameter id.");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "validation/v2/editForm";
        }

        log.info("updateParam = {}", updateParam);

        itemRepository.update(updateParam.getId(), updateParam);

        return "redirect:/validation/v2/items/{id}";
    }
}
