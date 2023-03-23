package com.example.itemservice.web.validation;

import com.example.itemservice.domain.item.ItemParam;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemParamValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ItemParam.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemParam itemParam = (ItemParam) target;

        if (!StringUtils.hasText(itemParam.getItemName())) {
            errors.rejectValue("itemName", "required");
        }
        if ((itemParam.getPrice() == null || itemParam.getPrice() < 1000 || itemParam.getPrice() > 1000000)
            && errors.getFieldError("price") == null) {
            errors.rejectValue("price", "range", new Object[]{"1,000", "1,000,000"}, null);
        }
        if ((itemParam.getQuantity() == null || itemParam.getQuantity() < 1 || itemParam.getQuantity() > 9999)
            && errors.getFieldError("quantity") == null) {
            errors.rejectValue("quantity", "range", new Object[]{"1", "9,999"}, null);
        }
        if (itemParam.getPrice() != null && itemParam.getQuantity() != null
            && itemParam.getPrice() * itemParam.getQuantity() < 10000) {
            errors.reject("totalPriceMin", new Object[]{"10,000"}, null);
        }
    }
}
