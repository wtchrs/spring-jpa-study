package com.example.itemservice.web.validation.response;

import lombok.Getter;

@Getter
public class SaveResult {

    private Long id;

    public SaveResult(Long id) {
        this.id = id;
    }
}
