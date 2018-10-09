package com.marta.logistika.controller;

import com.marta.logistika.dto.DriverRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

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

        binder.registerCustomEditor(CityEntity.class, "location", new PropertyEditorSupport() {

            public void setAsText(String text) {
                Long id = Long.parseLong(text);
                CityEntity location = cityService.findById(id);
                setValue(location);
            }

            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    CityEntity location = (CityEntity) value;
                    return location.getName();
                }
                return null;
            }
        });

        binder.registerCustomEditor(CityEntity.class, "fromCity", new PropertyEditorSupport() {

            public void setAsText(String text) {
                Long id = Long.parseLong(text);
                CityEntity toCity = cityService.findById(id);
                setValue(toCity);
            }

            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    CityEntity city = (CityEntity) value;
                    return city.getName();
                }
                return null;
            }
        });

        binder.registerCustomEditor(CityEntity.class, "toCity", new PropertyEditorSupport() {

            public void setAsText(String text) {
                Long id = Long.parseLong(text);
                CityEntity toCity = cityService.findById(id);
                setValue(toCity);
            }

            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    CityEntity city = (CityEntity) value;
                    return city.getName();
                }
                return null;
            }
        });

        binder.registerCustomEditor(DriverRecord.class, "personalId", new PropertyEditorSupport() {

            public void setAsText(String personalId) {
                DriverRecord driver= driverService.findDriverByPersonalId(personalId);
                setValue(driver);
            }

            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    DriverRecord driver = (DriverRecord) value;
                    return driver.getPersonalId();
                }
                return null;
            }
        });
    }
}
