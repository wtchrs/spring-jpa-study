package com.example.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ItemParam {

    private String itemName;
    private Integer price;
    private Integer quantity;

    private Boolean open;
    private List<String> regions;
    private ItemType itemType;
    private String deliveryCode;

    public ItemParam() {
    }

    public ItemParam(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public ItemParam(String itemName, Integer price, Integer quantity, Boolean open, List<String> regions,
                     ItemType itemType, String deliveryCode) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.open = open;
        this.regions = regions;
        this.itemType = itemType;
        this.deliveryCode = deliveryCode;
    }

    public Item toItem() {
        return new Item(itemName, price, quantity, open, regions, itemType, deliveryCode);
    }
}
