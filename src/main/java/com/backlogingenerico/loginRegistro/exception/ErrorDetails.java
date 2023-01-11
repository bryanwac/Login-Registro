package com.backlogingenerico.loginRegistro.exception;

import java.util.Date;
import java.util.List;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private List<String> messages;
    private String details;
    private Throwable exception;
    private Integer status;

    public ErrorDetails() {
    }

    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public ErrorDetails(Date timestamp, String message, String details, Throwable exception) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.exception = exception;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
