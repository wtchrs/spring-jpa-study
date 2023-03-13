package com.example.itemservice.web.form;

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
@RequestMapping("/form/items")
public class FormItemController {

    private final static Map<String, String> REGION_MAP;
    private final static List<DeliveryCode> DELIVERY_CODES;

    private final ItemRepository itemRepository;

    static {
        REGION_MAP = Map.of(
                "SEOUL", "서울",
                "BUSAN", "부산",
                "JEJU", "제주"
        );

        DELIVERY_CODES = List.of(
                new DeliveryCode("FAST", "Fast delivery"),
                new DeliveryCode("NORMAL", "Normal delivery"),
                new DeliveryCode("SLOW", "Slow delivery")
        );
    }

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        return REGION_MAP;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        return DELIVERY_CODES;
    }

    @GetMapping
    public String items(Model model) {
        log.info("BasicItemController.items");

        List<Item> items = itemRepository.findAll();
        log.info("items = {}", items);
        model.addAttribute("items", items);

        return "form/items";
    }

    @GetMapping("/{id}")
    public String itemDetail(Model model, @PathVariable Long id) {
        log.info("BasicItemController.itemDetail");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("item") ItemParam item) {
        log.info("BasicItemController.addForm");
        return "form/addForm";
    }

    @PostMapping("/add")
    public String addProcess(@ModelAttribute("item") ItemParam itemParam, RedirectAttributes redirectAttributes) {
        log.info("BasicItemController.addProcess");

        log.info("itemParam.getOpen() = {}", itemParam.getOpen());
        log.info("itemParam.getRegions() = {}", itemParam.getRegions());
        log.info("itemParam.getItemType() = {}", itemParam.getItemType());
        log.info("itemParam.getDeliveryCode() = {}", itemParam.getDeliveryCode());

        Item savedItem = itemRepository.save(itemParam.toItem());
        log.info("savedItem = {}", savedItem);

        redirectAttributes.addAttribute("id", savedItem.getId());
        redirectAttributes.addAttribute("saved", true);

        return "redirect:/form/items/{id}";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, @PathVariable Long id) {
        log.info("BasicItemController.editForm");

        Item item = itemRepository.findById(id);
        log.info("item = {}", item);
        model.addAttribute("item", item);

        return "form/editForm";
    }

    @PostMapping("/{id}/edit")
    public String editProcess(@PathVariable Long id,
                              @RequestParam("id") Long paramId,
                              @ModelAttribute ItemParam updateParam) {
        log.info("BasicItemController.editProcess");
        if (!id.equals(paramId)) {
            throw new IllegalArgumentException("Path variable id not equal to request parameter id.");
        }

        log.info("updateParam.getOpen() = {}", updateParam.getOpen());

        log.info("paramId = {}", paramId);
        log.info("updateParam = {}", updateParam);

        itemRepository.update(paramId, updateParam);

        return "redirect:/form/items/{id}";
    }
}
