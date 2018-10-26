package com.marta.logistika.messaging;

import java.io.Serializable;

public interface MessageSender {

    void sendMessage();

    void sendMessage(final String message);

    void sendMessage(final Serializable message);
}
