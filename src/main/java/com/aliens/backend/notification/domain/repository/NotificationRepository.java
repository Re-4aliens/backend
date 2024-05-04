package com.aliens.backend.notification.domain.repository;

import com.aliens.backend.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT * FROM Notification nc WHERE nc.member_id = :memberId AND nc.is_read = false ORDER BY nc.id DESC LIMIT 20", nativeQuery = true)
    List<Notification> findNotificationsByMemberId(Long memberId);
}
