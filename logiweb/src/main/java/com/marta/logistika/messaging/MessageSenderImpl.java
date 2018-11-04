package com.marta.logistika.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
public class MessageSenderImpl implements MessageSender {

    private final JmsTemplate jmsTemplate;
    private static final String DEFAULT_MESSAGE = "update";

    @Autowired
    public MessageSenderImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendMessage() {
        sendMessage(DEFAULT_MESSAGE);
    }

    @Override
    public void sendMessage(final String message) {
        jmsTemplate.send(session -> session.createTextMessage(message));
    }

    @Override
    public void sendMessage(final Serializable message) {
        jmsTemplate.send(session -> session.createObjectMessage(message));
    }
}