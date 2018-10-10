package com.marta.logistika.validator;

import com.marta.logistika.service.api.DriverService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePersonalIdValidator implements ConstraintValidator<UniquePersonalId, String> {

    @Autowired
    private DriverService driverService;

    @Override
    public boolean isValid(String personalId, ConstraintValidatorContext context) {
        return !driverService.personalIdExists(personalId);
    }

}
