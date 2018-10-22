package com.marta.logistika.controller;

import com.marta.logistika.dto.RoadRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.RoadService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(MockitoJUnitRunner.class)
public class DestinationsControllerTest {

    private DestinationsController destinationsController;
    @Mock private CityService cityService;
    @Mock private RoadService roadService;

    private CityEntity moscow;
    private List<CityEntity> listAll;

    @Before
    public void setup() {
        destinationsController = new DestinationsController(cityService, roadService);
        prefillTestData();
        defineMocks();
    }

    @After
    public void cleanup() {
        Mockito.reset();
    }

    @Test
    public void home() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.destinationsController).build();
        mockMvc.perform(get("/destinations"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("cities", listAll))
                .andExpect(forwardedUrl("office/destinations/list"))
        ;
        verify(cityService, times(1)).listAll();
        verifyNoMoreInteractions(cityService);
    }

//    @Test
//    public void editCity() throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.destinationsController).build();
//        mockMvc.perform(get("/destinations/1"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("city", moscow))
//                .andExpect(model().attribute("roads", Mockito.anyCollectionOf(RoadRecord.class)))
//                .andExpect(forwardedUrl("office/destinations/edit"))
//        ;
//    }

    private void prefillTestData() {
        moscow = new CityEntity();
        moscow.setId(1);
        moscow.setName("Москва");

        listAll = Arrays.asList(moscow);
    }

    private void defineMocks() {
        when(cityService.listAll()).thenReturn(listAll);
        when(cityService.findById(1L)).thenReturn(moscow);
        when(roadService.listAllRoadsFrom(moscow)).thenReturn(new ArrayList<RoadRecord>());
    }
}
