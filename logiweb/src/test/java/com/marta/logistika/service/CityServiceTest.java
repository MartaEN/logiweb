package com.marta.logistika.service;

import com.marta.logistika.dao.api.CityDao;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.exception.checked.DuplicateCityException;
import com.marta.logistika.exception.unchecked.EntityNotFoundException;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.impl.CityServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

    @Mock
    private CityDao cityDao;
    private CityService cityService;

    private CityEntity moscow;
    private CityEntity tver;
    private CityEntity pskov;

    @Before
    public void setup() {
        cityService = new CityServiceImpl(cityDao);
        prefillTestData();
        defineMocks();
    }

    @Test
    public void addCity() throws DuplicateCityException {
        CityEntity newCity = new CityEntity();
        newCity.setName("Санкт-Петербург");
        cityService.add(newCity);
        Mockito.verify(cityDao, VerificationModeFactory.times(1)).add(Mockito.any(CityEntity.class));
    }

    @Test(expected = DuplicateCityException.class)
    public void addDuplicateCity() throws DuplicateCityException {
        CityEntity newCity = new CityEntity();
        newCity.setName("Москва");
        cityService.add(newCity);
        Mockito.verify(cityDao, VerificationModeFactory.times(1)).add(Mockito.any(CityEntity.class));
    }

    @Test
    public void findById() {
        CityEntity city = cityService.findById(1L);
        Assert.assertEquals(moscow, city);
    }

    @Test (expected = EntityNotFoundException.class)
    public void findNonexistentCityById() {
        cityService.findById(100L);
    }

    @Test
    public void removeCity() {
        cityService.remove(1L);
        Mockito.verify(cityDao, VerificationModeFactory.times(1)).remove(moscow);
    }

    @Test (expected = EntityNotFoundException.class)
    public void removeNonexistentCity() {
        cityService.remove(100L);
    }

    @Test
    public void listAll() {
        cityService.listAll();
        Mockito.verify(cityDao, VerificationModeFactory.times(1)).listAll();
    }

    private void prefillTestData () {
        moscow = new CityEntity();
        moscow.setId(1);
        moscow.setName("Москва");

        tver = new CityEntity();
        tver.setId(2);
        tver.setName("Тверь");

        pskov = new CityEntity();
        pskov.setId(6);
        pskov.setName("Псков");
    }

    private void defineMocks() {
        Mockito.when(cityDao.findById(1L)).thenReturn(moscow);
        Mockito.when(cityDao.findById(2L)).thenReturn(tver);
        Mockito.when(cityDao.findById(6L)).thenReturn(pskov);
        Mockito.when(cityDao.cityNameExists("Москва")).thenReturn(true);
    }
}
