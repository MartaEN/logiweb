package com.marta.logistika.validator;

import com.marta.logistika.service.api.DriverService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private DriverService driverService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !driverService.usernameExists(username);
    }

}
