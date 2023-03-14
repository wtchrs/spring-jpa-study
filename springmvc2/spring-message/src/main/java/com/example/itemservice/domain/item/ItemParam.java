package com.example.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemParam {

    private String itemName;
    private Integer price;
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
}
