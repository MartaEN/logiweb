package com.marta.logistika.tableau.view;

import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Named
@Startup
@Stateful
@ApplicationScoped
public class TableauView extends AbstractView {

    public String getTestMessage() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8088/rest/test");
        return target.request(MediaType.APPLICATION_JSON).get(String.class);
    }

}
