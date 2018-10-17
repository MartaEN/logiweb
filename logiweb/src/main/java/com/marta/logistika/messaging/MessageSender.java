package com.marta.logistika.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

@Component
public class MessageSender {

	private final JmsTemplate jmsTemplate;

	@Autowired
	public MessageSender(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void sendMessage(final String message) {

		jmsTemplate.send(new MessageCreator(){
				@Override
				public Message createMessage(Session session) throws JMSException{
					TextMessage textMessage = session.createTextMessage(message);
					return textMessage;
				}
			});
	}

	public void sendMessage(final Serializable message) {

		jmsTemplate.send(new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException{
				ObjectMessage objectMessage = session.createObjectMessage(message);
				return objectMessage;
			}
		});
	}

}
