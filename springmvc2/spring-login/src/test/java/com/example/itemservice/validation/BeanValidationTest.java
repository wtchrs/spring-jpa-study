package com.example.itemservice.validation;

import com.example.itemservice.web.item.form.ItemSaveForm;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanValidationTest {

    @Test
    void beanValidation() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        ItemSaveForm itemParam = new ItemSaveForm(" ", 10, 10000);

        Set<ConstraintViolation<ItemSaveForm>> violations = validator.validate(itemParam);
        for (ConstraintViolation<ItemSaveForm> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }

        assertThat(violations).hasSize(3);
    }
}
