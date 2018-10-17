package com.marta.logistika.tableau.beans;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class WebsocketPushBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(WebsocketPushBean.class.getName());

    @Inject
    @Push(channel = "websocket")
    private PushContext push;

    public void pushUpdate(String input) {
        push.send(input);
    }

    public void pushUpdate(Object input) {
        push.send(input);
    }
}
