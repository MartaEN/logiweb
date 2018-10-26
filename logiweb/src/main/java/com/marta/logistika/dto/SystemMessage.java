package com.marta.logistika.dto;

import java.io.Serializable;

public class SystemMessage implements Serializable {

    private String title;
    private String body;

    public SystemMessage() {
    }

    public SystemMessage(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
