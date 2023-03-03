package com.example.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemUpdateParam {

    private String itemName;
    private Integer price;
    private Integer quantity;

    public ItemUpdateParam() {
    }

    public ItemUpdateParam(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public Item toItem() {
        return new Item(itemName, price, quantity);
    }
}
