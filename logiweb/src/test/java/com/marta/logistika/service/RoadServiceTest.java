package com.marta.logistika.service;

import com.marta.logistika.dao.api.RoadDao;
import com.marta.logistika.dto.RoadRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;
import com.marta.logistika.exception.DuplicateRoadException;
import com.marta.logistika.exception.NoRouteFoundException;
import com.marta.logistika.service.api.RoadService;
import com.marta.logistika.service.impl.RoadServiceImpl;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RoadServiceTest {

    @Mock
    private RoadDao roadDao;
    private RoadService roadService;
    private static Mapper mapper = new DozerBeanMapper();

    private CityEntity moscow;
    private CityEntity tver;
    private CityEntity pskov;
    private CityEntity spb;
    private CityEntity yaroslavl;
    private CityEntity vologda;
    private RoadEntity roadMoscowTver;
    private RoadEntity roadTverMoscow;
    private RoadEntity roadTverSpb;
    private RoadEntity roadSpbTver;
    private RoadEntity roadMoscowPskov;
    private RoadEntity roadPskovMoscow;
    private RoadEntity roadSpbPskov;
    private RoadEntity roadPskovSpb;
    private List<RoadEntity> roadsToSpb = new ArrayList<>();
    private List<RoadEntity> roadsToTver = new ArrayList<>();
    private List<RoadEntity> roadsToMoscow = new ArrayList<>();
    private List<RoadEntity> roadsToPskov = new ArrayList<>();

    @Before
    public void setup() {
        roadService = new RoadServiceImpl(roadDao);

        prefillTestData();

        Mockito.when(roadDao.findById(1L)).thenReturn(roadMoscowTver);
        Mockito.when(roadDao.findById(2L)).thenReturn(roadTverMoscow);
        Mockito.when(roadDao.findById(3L)).thenReturn(roadTverSpb);
        Mockito.when(roadDao.findById(4L)).thenReturn(roadSpbTver);
        Mockito.when(roadDao.findById(5L)).thenReturn(roadMoscowPskov);
        Mockito.when(roadDao.findById(6L)).thenReturn(roadPskovMoscow);
        Mockito.when(roadDao.findById(7L)).thenReturn(roadPskovSpb);
        Mockito.when(roadDao.findById(8L)).thenReturn(roadSpbPskov);

        Mockito.when(roadDao.listAllRoadsTo(spb)).thenReturn(roadsToSpb);
        Mockito.when(roadDao.listAllRoadsTo(tver)).thenReturn(roadsToTver);
        Mockito.when(roadDao.listAllRoadsTo(moscow)).thenReturn(roadsToMoscow);
        Mockito.when(roadDao.listAllRoadsTo(pskov)).thenReturn(roadsToPskov);

        Mockito.when(roadDao.getDirectRoadFromTo(tver, moscow)).thenReturn(roadTverMoscow);
        Mockito.when(roadDao.getDirectRoadFromTo(moscow, tver)).thenReturn(roadMoscowTver);

    }


    @Test
    public void distanceFinding() {
        int distance = roadService.getDistanceFromTo(moscow, spb);
        Assert.assertEquals(700, distance);
    }

    @Test
    public void zeroDistanceFinding() {
        int distance = roadService.getDistanceFromTo(moscow, moscow);
        Assert.assertEquals(0, distance);
    }

    @Test(expected = NoRouteFoundException.class)
    public void missingRoadDistanceFinding() {
        roadService.getDistanceFromTo(moscow, yaroslavl);
    }

    @Test
    public void routeSearch() {
        List<RoadRecord> route = roadService.findRouteFromTo(moscow, spb);
        List<RoadRecord> routeExpected = new ArrayList<>();
        routeExpected.add(mapper.map(roadMoscowTver, RoadRecord.class));
        routeExpected.add(mapper.map(roadTverSpb, RoadRecord.class));
        Assert.assertEquals(routeExpected, route);
    }

    @Test(expected = NoRouteFoundException.class)
    public void missingRouteSearch() {
        roadService.findRouteFromTo(moscow, yaroslavl);
    }

    @Test
    public void routeDistanceFinding() {
        List<CityEntity> testRoute = new ArrayList<>();
        testRoute.add(moscow);
        testRoute.add(spb);
        testRoute.add(tver);
        Assert.assertEquals(1200, roadService.getRouteDistance(testRoute));
    }

    @Test(expected = NoRouteFoundException.class)
    public void missingRouteDistanceFinding() {
        List<CityEntity> testRoute = new ArrayList<>();
        testRoute.add(moscow);
        testRoute.add(spb);
        testRoute.add(yaroslavl);
        roadService.getRouteDistance(testRoute);
    }

    @Test
    public void newRoadCreation () {
        RoadEntity road = new RoadEntity();
        road.setFromCity(yaroslavl);
        road.setToCity(vologda);
        road.setDistance(200);
        roadService.add(road);
        Mockito.verify(roadDao, VerificationModeFactory.times(2)).add(Mockito.any(RoadEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRoadCreation1 () {
        RoadEntity road = new RoadEntity();
        road.setFromCity(null);
        road.setToCity(vologda);
        road.setDistance(200);
        roadService.add(road);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRoadCreation2 () {
        RoadEntity road = new RoadEntity();
        road.setFromCity(yaroslavl);
        road.setToCity(vologda);
        road.setDistance(0);
        roadService.add(road);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRoadCreation3 () {
        roadService.add(null);
    }

    @Test(expected = DuplicateRoadException.class)
    public void duplicatedRoadCreation () {
        RoadEntity road = new RoadEntity();
        road.setFromCity(moscow);
        road.setToCity(tver);
        road.setDistance(100);
        roadService.add(road);
    }

    @Test
    public void roadDeletion () {
        roadService.remove(1L);
        Mockito.verify(roadDao, VerificationModeFactory.times(1)).remove(roadMoscowTver);
        Mockito.verify(roadDao, VerificationModeFactory.times(1)).remove(roadTverMoscow);
    }

    @Test
    public void listAll () {
        roadService.listAll();
        Mockito.verify(roadDao, VerificationModeFactory.times(1)).listAll();
    }

    @Test
    public void listAllRoadsFrom () {
        roadService.listAllRoadsFrom(moscow);
        Mockito.verify(roadDao, VerificationModeFactory.times(1)).listAllRoadsFrom(moscow);
    }

    private void prefillTestData() {

        moscow = new CityEntity();
        moscow.setId(1);
        moscow.setName("Москва");

        tver = new CityEntity();
        tver.setId(2);
        tver.setName("Тверь");

        spb = new CityEntity();
        spb.setId(3);
        spb.setName("Санкт-Петербург");

        yaroslavl = new CityEntity();
        yaroslavl.setId(4);
        yaroslavl.setName("Ярославль");

        vologda = new CityEntity();
        vologda.setId(5);
        vologda.setName("Вологда");

        pskov = new CityEntity();
        pskov.setId(6);
        pskov.setName("Псков");

        roadMoscowTver = new RoadEntity();
        roadMoscowTver.setId(1L);
        roadMoscowTver.setFromCity(moscow);
        roadMoscowTver.setToCity(tver);
        roadMoscowTver.setDistance(200);

        roadTverMoscow = new RoadEntity();
        roadTverMoscow.setId(2L);
        roadTverMoscow.setFromCity(tver);
        roadTverMoscow.setToCity(moscow);
        roadTverMoscow.setDistance(200);

        roadTverSpb = new RoadEntity();
        roadTverSpb.setId(3L);
        roadTverSpb.setFromCity(tver);
        roadTverSpb.setToCity(spb);
        roadTverSpb.setDistance(500);

        roadSpbTver = new RoadEntity();
        roadSpbTver.setId(4L);
        roadSpbTver.setFromCity(spb);
        roadSpbTver.setToCity(tver);
        roadSpbTver.setDistance(500);

        roadMoscowPskov = new RoadEntity();
        roadMoscowPskov.setId(5L);
        roadMoscowPskov.setFromCity(moscow);
        roadMoscowPskov.setToCity(pskov);
        roadMoscowPskov.setDistance(700);

        roadPskovMoscow= new RoadEntity();
        roadPskovMoscow.setId(6L);
        roadPskovMoscow.setFromCity(pskov);
        roadPskovMoscow.setToCity(moscow);
        roadPskovMoscow.setDistance(700);

        roadPskovSpb = new RoadEntity();
        roadPskovSpb.setId(7L);
        roadPskovSpb.setFromCity(pskov);
        roadPskovSpb.setToCity(spb);
        roadPskovSpb.setDistance(250);

        roadSpbPskov= new RoadEntity();
        roadSpbPskov.setId(8L);
        roadSpbPskov.setFromCity(spb);
        roadSpbPskov.setToCity(pskov);
        roadSpbPskov.setDistance(250);

        roadsToSpb.add(roadPskovSpb);
        roadsToSpb.add(roadTverSpb);
        roadsToTver.add(roadMoscowTver);
        roadsToTver.add(roadSpbTver);
        roadsToMoscow.add(roadTverMoscow);
        roadsToMoscow.add(roadPskovMoscow);
        roadsToPskov.add(roadSpbPskov);
        roadsToPskov.add(roadMoscowPskov);

    }

}
