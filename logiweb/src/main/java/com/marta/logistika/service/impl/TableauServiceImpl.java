package com.marta.logistika.service.impl;

import com.marta.logistika.messaging.MessageSender;
import com.marta.logistika.service.api.TableauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tableauService")
public class TableauServiceImpl extends AbstractService implements TableauService {

    private final MessageSender messageSender;

    @Autowired
    public TableauServiceImpl(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void updateTableau() {
        messageSender.sendMessage("updated");
    }


}
