package com.example.itemservice.web.validation.form;

import com.example.itemservice.domain.item.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ToString
public class ItemUpdateForm {

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    private Integer quantity;

    public ItemUpdateForm() {
    }

    public ItemUpdateForm(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public static ItemUpdateForm from(Item item) {
        ItemUpdateForm itemParam = new ItemUpdateForm(item.getItemName(), item.getPrice(), item.getQuantity());
        itemParam.setId(item.getId());
        return itemParam;
    }
}
