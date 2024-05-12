package com.aliens.backend.notification.domain.repository;

import com.aliens.backend.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT * FROM Notification  WHERE Notification.memberId = :memberId AND Notification.isRead = false ORDER BY Notification.notificationId DESC LIMIT 20", nativeQuery = true)
    List<Notification> findNotificationsByMemberId(Long memberId);
}
