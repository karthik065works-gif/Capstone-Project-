package com.icici.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.icici.dto.NotificationResponse;
import com.icici.exception.CustomerNotFoundException;
import com.icici.exception.NotificationNotFoundException;
import com.icici.model.Customer;
import com.icici.model.Notification;
import com.icici.model.NotificationStatus;
import com.icici.model.NotificationType;
import com.icici.repository.CustomerRepository;
import com.icici.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CustomerRepository customerRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            CustomerRepository customerRepository) {

        this.notificationRepository = notificationRepository;
        this.customerRepository = customerRepository;
    }

    // =========================================================
    // CREATE NOTIFICATION
    // =========================================================

    public Notification createNotification(
            Long customerId,
            NotificationType notificationType,
            String message,
            String transactionReference) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: "
                                        + customerId
                        )
                );

        Notification notification =
                new Notification();

        notification.setNotificationReference(
                generateNotificationReference()
        );

        notification.setCustomer(customer);

        notification.setNotificationType(
                notificationType
        );

        notification.setStatus(
                NotificationStatus.UNREAD
        );

        notification.setMessage(message);

        notification.setTransactionReference(
                transactionReference
        );

        notification.setCreatedAt(
                LocalDateTime.now()
        );

        return notificationRepository.save(
                notification
        );
    }

    // =========================================================
    // GET ALL CUSTOMER NOTIFICATIONS
    // =========================================================

    public List<NotificationResponse>
    getCustomerNotifications(Long customerId) {

        validateCustomer(customerId);

        return notificationRepository
                .findByCustomerIdOrderByCreatedAtDesc(
                        customerId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET UNREAD NOTIFICATIONS
    // =========================================================

    public List<NotificationResponse>
    getUnreadNotifications(Long customerId) {

        validateCustomer(customerId);

        return notificationRepository
                .findByCustomerIdAndStatusOrderByCreatedAtDesc(
                        customerId,
                        NotificationStatus.UNREAD
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET READ NOTIFICATIONS
    // =========================================================

    public List<NotificationResponse>
    getReadNotifications(Long customerId) {

        validateCustomer(customerId);

        return notificationRepository
                .findByCustomerIdAndStatusOrderByCreatedAtDesc(
                        customerId,
                        NotificationStatus.READ
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET UNREAD COUNT
    // =========================================================

    public long getUnreadCount(Long customerId) {

        validateCustomer(customerId);

        return notificationRepository
                .countByCustomerIdAndStatus(
                        customerId,
                        NotificationStatus.UNREAD
                );
    }

    // =========================================================
    // GET NOTIFICATION BY REFERENCE
    // =========================================================

    public NotificationResponse
    getNotificationByReference(
            String notificationReference) {

        Notification notification =
                findNotification(
                        notificationReference
                );

        return toResponse(notification);
    }

    // =========================================================
    // MARK NOTIFICATION AS READ
    // =========================================================

    public NotificationResponse markAsRead(
            String notificationReference) {

        Notification notification =
                findNotification(
                        notificationReference
                );

        notification.setStatus(
                NotificationStatus.READ
        );

        Notification savedNotification =
                notificationRepository.save(
                        notification
                );

        return toResponse(savedNotification);
    }

    // =========================================================
    // MARK ALL AS READ
    // =========================================================

    public void markAllAsRead(Long customerId) {

        validateCustomer(customerId);

        List<Notification> notifications =
                notificationRepository
                        .findByCustomerIdAndStatusOrderByCreatedAtDesc(
                                customerId,
                                NotificationStatus.UNREAD
                        );

        for (Notification notification :
                notifications) {

            notification.setStatus(
                    NotificationStatus.READ
            );
        }

        notificationRepository.saveAll(
                notifications
        );
    }

    // =========================================================
    // DELETE NOTIFICATION
    // =========================================================

    public void deleteNotification(
            String notificationReference) {

        Notification notification =
                findNotification(
                        notificationReference
                );

        notificationRepository.delete(
                notification
        );
    }

    // =========================================================
    // FIND NOTIFICATION
    // =========================================================

    private Notification findNotification(
            String notificationReference) {

        return notificationRepository
                .findByNotificationReference(
                        notificationReference
                )
                .orElseThrow(() ->
                        new NotificationNotFoundException(
                                "Notification not found with reference: "
                                        + notificationReference
                        )
                );
    }

    // =========================================================
    // VALIDATE CUSTOMER
    // =========================================================

    private void validateCustomer(Long customerId) {

        if (!customerRepository.existsById(
                customerId)) {

            throw new CustomerNotFoundException(
                    "Customer not found with id: "
                            + customerId
            );
        }
    }

    // =========================================================
    // ENTITY → DTO
    // =========================================================

    private NotificationResponse toResponse(
            Notification notification) {

        NotificationResponse response =
                new NotificationResponse();

        response.setId(
                notification.getId()
        );

        response.setNotificationReference(
                notification.getNotificationReference()
        );

        response.setCustomerId(
                notification.getCustomer().getId()
        );

        response.setNotificationType(
                notification.getNotificationType()
        );

        response.setStatus(
                notification.getStatus()
        );

        response.setMessage(
                notification.getMessage()
        );

        response.setTransactionReference(
                notification.getTransactionReference()
        );

        response.setCreatedAt(
                notification.getCreatedAt()
        );

        return response;
    }

    // =========================================================
    // GENERATE NOTIFICATION REFERENCE
    // =========================================================

    private String generateNotificationReference() {

        return "NOT-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}