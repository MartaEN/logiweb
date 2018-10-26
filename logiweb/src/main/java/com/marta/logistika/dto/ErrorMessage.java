package com.marta.logistika.dto;

import java.io.Serializable;

public class ErrorMessage implements Serializable {
    private String title;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
