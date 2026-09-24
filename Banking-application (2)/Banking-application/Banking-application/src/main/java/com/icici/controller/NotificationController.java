package com.icici.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dto.NotificationResponse;
import com.icici.service.NotificationService;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService =
                notificationService;
    }

    // =========================================================
    // GET ALL NOTIFICATIONS FOR CUSTOMER
    // =========================================================

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<NotificationResponse>>
    getCustomerNotifications(
            @PathVariable Long customerId) {

        List<NotificationResponse> notifications =
                notificationService
                        .getCustomerNotifications(
                                customerId
                        );

        return ResponseEntity.ok(
                notifications
        );
    }

    // =========================================================
    // GET UNREAD NOTIFICATIONS
    // =========================================================

    @GetMapping("/customer/{customerId}/unread")
    public ResponseEntity<List<NotificationResponse>>
    getUnreadNotifications(
            @PathVariable Long customerId) {

        List<NotificationResponse> notifications =
                notificationService
                        .getUnreadNotifications(
                                customerId
                        );

        return ResponseEntity.ok(
                notifications
        );
    }

    // =========================================================
    // GET READ NOTIFICATIONS
    // =========================================================

    @GetMapping("/customer/{customerId}/read")
    public ResponseEntity<List<NotificationResponse>>
    getReadNotifications(
            @PathVariable Long customerId) {

        List<NotificationResponse> notifications =
                notificationService
                        .getReadNotifications(
                                customerId
                        );

        return ResponseEntity.ok(
                notifications
        );
    }

    // =========================================================
    // GET UNREAD COUNT
    // =========================================================

    @GetMapping("/customer/{customerId}/unread/count")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable Long customerId) {

        long count =
                notificationService
                        .getUnreadCount(
                                customerId
                        );

        return ResponseEntity.ok(
                count
        );
    }

    // =========================================================
    // GET NOTIFICATION BY REFERENCE
    // =========================================================

    @GetMapping("/{notificationReference}")
    public ResponseEntity<NotificationResponse>
    getNotificationByReference(
            @PathVariable String notificationReference) {

        NotificationResponse notification =
                notificationService
                        .getNotificationByReference(
                                notificationReference
                        );

        return ResponseEntity.ok(
                notification
        );
    }

    // =========================================================
    // MARK ONE NOTIFICATION AS READ
    // =========================================================

    @PatchMapping("/{notificationReference}/read")
    public ResponseEntity<NotificationResponse>
    markAsRead(
            @PathVariable String notificationReference) {

        NotificationResponse notification =
                notificationService
                        .markAsRead(
                                notificationReference
                        );

        return ResponseEntity.ok(
                notification
        );
    }

    // =========================================================
    // MARK ALL CUSTOMER NOTIFICATIONS AS READ
    // =========================================================

    @PatchMapping("/customer/{customerId}/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable Long customerId) {

        notificationService.markAllAsRead(
                customerId
        );

        return ResponseEntity.noContent()
                .build();
    }

    // =========================================================
    // DELETE NOTIFICATION
    // =========================================================

    @DeleteMapping("/{notificationReference}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable String notificationReference) {

        notificationService.deleteNotification(
                notificationReference
        );

        return ResponseEntity.noContent()
                .build();
    }
}