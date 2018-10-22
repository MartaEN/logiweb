package com.marta.logistika.exception.checked;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class CheckedServiceException extends Exception {

    public CheckedServiceException(String message) {
        super(message);
    }

    public CheckedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckedServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Helper method to cope with encoding in Resource bundle
     * @param locale user locale
     * @return message from the "messages" resource bundle in UTF-8
     */
    String getLocalizedLabel(Locale locale) {
        ResourceBundle labels = ResourceBundle.getBundle("messages", locale);
        String label = labels.getString(getMessage());
        try {
            return new String(label.getBytes("ISO-8859-1"), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported", e);
        }
    }

    /**
     * Returns error message in user locale
     * @param locale user locale
     * @return localized message
     */
    public String getLocalizedMessage(Locale locale) {
        return getLocalizedLabel(locale);
    }
}