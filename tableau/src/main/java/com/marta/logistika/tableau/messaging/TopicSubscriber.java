package com.marta.logistika.tableau.messaging;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "logiweb.update"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class TopicSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                System.out.println("TextMessage received: " + ((TextMessage) message).getText());
            } else if (message instanceof ObjectMessage) {
                System.out.println("ObjectMessage received.");
            } else {
                System.out.println("Unknown message type:" + message.getClass());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
