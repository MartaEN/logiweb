package com.marta.logistika.controllerAdvice;

import com.marta.logistika.dto.DriverRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class DataBindingControllerAdvice {

    private final CityService cityService;
    private final DriverService driverService;

    @Autowired
    public DataBindingControllerAdvice(CityService cityService, DriverService driverService) {
        this.cityService = cityService;
        this.driverService = driverService;
    }

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder) {

        binder.registerCustomEditor(CityEntity.class, new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                Long id = Long.parseLong(text);
                CityEntity location = cityService.findById(id);
                setValue(location);
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    CityEntity location = (CityEntity) value;
                    return location.getName();
                }
                return null;
            }
        });

        binder.registerCustomEditor(DriverRecord.class, new PropertyEditorSupport() {

            @Override
            public void setAsText(String personalId) {
                DriverRecord driver = driverService.findDriverByPersonalId(personalId);
                setValue(driver);
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    DriverRecord driver = (DriverRecord) value;
                    return driver.getPersonalId();
                }
                return null;
            }
        });

        binder.registerCustomEditor(LocalDateTime.class, "departureDateTime", new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                LocalDateTime departureDateTime = LocalDateTime.parse(
                        text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                setValue(departureDateTime);
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    LocalDateTime departureDateTime = (LocalDateTime) value;
                    return departureDateTime.toString();
                }
                return null;
            }
        });

    }
}
