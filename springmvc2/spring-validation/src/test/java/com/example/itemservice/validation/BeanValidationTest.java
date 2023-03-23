package com.example.itemservice.validation;

import com.example.itemservice.domain.item.ItemParam;
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

        ItemParam itemParam = new ItemParam(" ", 10, 10000);

        Set<ConstraintViolation<ItemParam>> violations = validator.validate(itemParam);
        for (ConstraintViolation<ItemParam> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }

        assertThat(violations).hasSize(3);
    }
}
