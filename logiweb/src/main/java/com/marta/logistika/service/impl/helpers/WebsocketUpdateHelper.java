package com.marta.logistika.service.impl.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class WebsocketUpdateHelper implements ApplicationListener<BrokerAvailabilityEvent> {

    private final SimpMessageSendingOperations messagingTemplate;
    private AtomicBoolean brokerAvailable = new AtomicBoolean();

    @Autowired
    public WebsocketUpdateHelper(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        this.brokerAvailable.set(event.isBrokerAvailable());
    }

    public void sendUpdate(String username, Object message) {
        if(brokerAvailable.get()) {
            this.messagingTemplate.convertAndSendToUser(username, "/logiweb/updates", message);
        }
    }
}
