package com.something.identity_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        long years = ChronoUnit.YEARS.between(value, LocalDate.now());

        if (years < min) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("Age must be as least %d years old", min)
            ).addConstraintViolation();
            return false;
        }

        return true;
    }

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
}
