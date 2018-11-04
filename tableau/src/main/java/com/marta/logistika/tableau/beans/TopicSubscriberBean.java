package com.marta.logistika.tableau.beans;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "logiweb.update"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class TopicSubscriberBean implements MessageListener {

    private static final Logger LOG = Logger.getLogger(WebsocketPushBean.class.getName());


    @Inject
    private WebsocketPushBean pushBean;


    @Override
    public void onMessage(Message message) {
        LOG.info("Received message from logiweb.update channel");
        pushBean.pushUpdate();
    }
}
