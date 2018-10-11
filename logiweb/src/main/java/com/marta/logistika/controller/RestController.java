package com.marta.logistika.controller;

import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.service.api.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest")
public class RestController {

    private final TruckService truckService;

    @Autowired
    public RestController(TruckService truckService) {
        this.truckService = truckService;
    }

    @GetMapping(value = "/test", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String provideTestData () {
        return "Hello from logiweb!";
    }

}
