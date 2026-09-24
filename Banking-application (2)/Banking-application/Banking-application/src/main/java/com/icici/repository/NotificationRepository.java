package com.icici.repository;

import com.icici.model.Notification;
import com.icici.model.NotificationStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    Optional<Notification> findByNotificationReference(
            String notificationReference
    );

    List<Notification> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId
    );

    List<Notification> findByCustomerIdAndStatusOrderByCreatedAtDesc(
            Long customerId,
            NotificationStatus status
    );

    long countByCustomerIdAndStatus(
            Long customerId,
            NotificationStatus status
    );

    boolean existsByNotificationReference(
            String notificationReference
    );
}