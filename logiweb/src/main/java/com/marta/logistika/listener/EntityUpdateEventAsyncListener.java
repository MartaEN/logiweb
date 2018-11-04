package com.marta.logistika.listener;

import com.marta.logistika.event.EntityUpdateEvent;
import com.marta.logistika.messaging.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EntityUpdateEventAsyncListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityUpdateEventAsyncListener.class);
    private final MessageSender messageSender;

    public EntityUpdateEventAsyncListener(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @TransactionalEventListener
    @Async
    public void processEntityEvent(EntityUpdateEvent event) {
        LOGGER.debug("###LOGIWEB### entity update ({}) detected and message being sent", event);
        messageSender.sendMessage();
    }
}
