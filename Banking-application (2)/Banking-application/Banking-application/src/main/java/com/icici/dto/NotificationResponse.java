package com.icici.dto;

import java.time.LocalDateTime;

import com.icici.model.NotificationStatus;
import com.icici.model.NotificationType;

public class NotificationResponse {

    private Long id;

    private String notificationReference;

    private Long customerId;

    private NotificationType notificationType;

    private NotificationStatus status;

    private String message;

    private String transactionReference;

    private LocalDateTime createdAt;

    public NotificationResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationReference() {
        return notificationReference;
    }

    public void setNotificationReference(
            String notificationReference) {

        this.notificationReference =
                notificationReference;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(
            NotificationType notificationType) {

        this.notificationType =
                notificationType;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(
            NotificationStatus status) {

        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(
            String transactionReference) {

        this.transactionReference =
                transactionReference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }
}