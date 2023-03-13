package com.example.itemservice.domain.item;

import lombok.Getter;

@Getter
public enum ItemType {

    BOOK("Book"), FOOD("Food"), ETC("Etc");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

}
