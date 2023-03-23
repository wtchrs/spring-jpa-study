package com.example.itemservice.domain.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ToString
public class ItemParam {

    @NotNull(groups = UpdateCheck.class)
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1, max = 9999, groups = SaveCheck.class)
    private Integer quantity;

    public ItemParam() {
    }

    public ItemParam(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public Item toItem() {
        return new Item(itemName, price, quantity);
    }

    public static ItemParam from(Item item) {
        ItemParam itemParam = new ItemParam(item.getItemName(), item.getPrice(), item.getQuantity());
        itemParam.setId(item.getId());
        return itemParam;
    }
}
