package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.RoadDao;
import com.marta.logistika.dto.RoadRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;
import com.marta.logistika.exception.EntityNotFoundException;
import com.marta.logistika.service.api.RoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service("roadService")
public class RoadServiceImpl extends AbstractService implements RoadService {

    private final RoadDao roadDao;

    @Autowired
    public RoadServiceImpl(RoadDao roadDao) {
        this.roadDao = roadDao;
    }

    @Override
    @Transactional
    public void add(RoadEntity road) {

        //check road parameters validity
        if(road == null) return;
        if(road.getFromCity() == null || road.getToCity() == null) throw new IllegalArgumentException("Road id" + road.getId() + " invalid: null end points");
        if(road.getFromCity().equals(road.getToCity())) throw new IllegalArgumentException("Road id" + road.getId() + " invalid: coinciding end points");
//        if(roadDao.getDirectRoadFromTo(road.getFromCity(), road.getToCity()) != null) throw new SuchEntityAlreadyExistsException(road.getId(), RoadEntity.class);
        if(road.getDistance() < 0.1f) throw new IllegalArgumentException("Road id" + road.getId() + ": distance " + road.getDistance() + " invalid; should be positive");

        //create return road
        RoadEntity returnRoad = new RoadEntity();
        returnRoad.setFromCity(road.getToCity());
        returnRoad.setToCity(road.getFromCity());
        returnRoad.setDistance(road.getDistance());

        //persist both roads
        roadDao.add(road);
        roadDao.add(returnRoad);
    }

    @Override
    @Transactional
    public void remove(long roadId) {
        //find requested road
        RoadEntity road = roadDao.findById(roadId);
        if (road == null) throw new EntityNotFoundException(roadId, RoadEntity.class);

        //find return road
        RoadEntity returnRoad = roadDao.getDirectRoadFromTo(road.getToCity(), road.getFromCity());

        //delete both roads
        roadDao.remove(road);
        roadDao.remove(returnRoad);
    }

    @Override
    public List<RoadRecord> listAll() {
        return roadDao.listAll()
                .stream()
                .map(r -> mapper.map(r, RoadRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoadRecord> listAllRoadsFrom(CityEntity city) {
        return roadDao.listAllRoadsFrom(city)
                .stream()
                .map(r -> mapper.map(r, RoadRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoadRecord> findRouteFromTo(CityEntity fromCity, CityEntity toCity) {

        class Route {
            private LinkedList<RoadEntity> roadList;
            private int distance;

            private Route() {
                roadList = new LinkedList<>();
                distance = 0;
            }

            private Route (Route route) {
                this.roadList = new LinkedList<>(route.roadList);
                this.distance = route.distance;
            }

            private Route addRoad (RoadEntity road) {
                roadList.addFirst(road);
                distance += road.getDistance();
                return this;
            }
        }

        Map<CityEntity, Route> routesFound = new HashMap<>();
        routesFound.put(toCity, new Route());

        LinkedList<CityEntity> wave = new LinkedList<>();
        wave.add(toCity);

        while(!wave.isEmpty()) {

            CityEntity currentCity = wave.removeFirst();
            Route currentCityRoute = routesFound.get(currentCity);

            roadDao.listAllRoadsTo(currentCity).forEach(nextRoad -> {

                CityEntity nextCity = nextRoad.getFromCity();
                Route nextCityRoute = new Route(currentCityRoute).addRoad(nextRoad);

                if( !routesFound.containsKey(nextCity)) {
                    routesFound.put(nextCity, nextCityRoute);
                    if (!nextCity.equals(fromCity)) wave.add(nextCity);
                } else if (nextCityRoute.distance < routesFound.get(nextCity).distance) {
                    routesFound.put(nextCity, nextCityRoute);
                }
            });
        }

        if(!routesFound.containsKey(fromCity)) throw new RuntimeException("No route available from " + fromCity.getName() + " to " + toCity.getName());


        return routesFound.get(fromCity).roadList
                .stream()
                .map(r -> mapper.map(r, RoadRecord.class))
                .collect(Collectors.toList());

    }

    @Override
    public int getDistanceFromTo(CityEntity fromCity, CityEntity toCity) {
        if (fromCity.equals(toCity)) return 0;
        return findRouteFromTo(fromCity, toCity)
                .stream()
                .mapToInt(RoadRecord::getDistance)
                .sum();
    }

    @Override
    public int getRouteDistance(List<CityEntity> route) {
        int distance = 0;
        for (int i = 1; i < route.size(); i++) {
            distance += getDistanceFromTo(route.get(i), route.get(i-1));
        }
        return distance;
    }
}
