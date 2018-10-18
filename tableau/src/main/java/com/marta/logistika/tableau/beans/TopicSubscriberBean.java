package com.marta.logistika.tableau.beans;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
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
//        try {
//            if (message instanceof TextMessage) {
//                String input = ((TextMessage) message).getText();
//                System.out.println("TextMessage received: " + input);
//            } else if (message instanceof ObjectMessage) {
//                System.out.println("ObjectMessage received.");
//            } else {
//                System.out.println("Unknown message type:" + message.getClass());
//            }
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }
    }
}
