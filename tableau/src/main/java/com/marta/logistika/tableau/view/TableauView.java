package com.marta.logistika.tableau.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marta.logistika.tableau.dto.OrderRecordShort;
import org.primefaces.model.chart.PieChartModel;

import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@Startup
@Stateful
@ApplicationScoped
public class TableauView extends AbstractView {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * gets list of latest orders from rest api
     * @return list of latest orders
     * @throws IOException for errors parsing the rest api response
     */
    public List<OrderRecordShort> getLatestOrders () throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8088/tableau/orders");
        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);
        return objectMapper.readValue(response, new TypeReference<List<OrderRecordShort>>(){});
    }

    /**
     * gets driver statistics from rest api and builds a donut chart model
     * @return donut chart model
     * @throws IOException for errors parsing the rest api response
     */
    public PieChartModel getDriverStatistics () throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8088/tableau/stats/drivers");
        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);
        LinkedHashMap<String, Integer> driverStats = objectMapper.readValue(response, new TypeReference<LinkedHashMap<String, Integer>>(){});
        int totalDrivers = driverStats.values().stream().mapToInt(Integer::intValue).sum();

        PieChartModel model = new PieChartModel();
        driverStats.forEach(model::set);
        model.setTitle(String.format("Drivers (total %d)", totalDrivers));
        model.setShowDataLabels(true);
//        model.setDataFormat("value");
        model.setLegendPosition("w");
        model.setShadow(true);

        return model;
    }

    /**
     * gets truck statistics from rest api and builds a donut chart model
     * @return donut chart model
     * @throws IOException for errors parsing the rest api response
     */
    public PieChartModel getTruckStatistics () throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8088/tableau/stats/trucks");
        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);
        LinkedHashMap<String, Integer> truckStats = objectMapper.readValue(response, new TypeReference<LinkedHashMap<String, Integer>>(){});
        int totalTrucks = truckStats.values().stream().mapToInt(Integer::intValue).sum();

        PieChartModel model = new PieChartModel();
        truckStats.forEach(model::set);
        model.setTitle(String.format("Trucks (total %d)", totalTrucks));
        model.setShowDataLabels(true);
//        model.setDataFormat("value");
        model.setLegendPosition("w");
        model.setShadow(true);

        return model;
    }

}
