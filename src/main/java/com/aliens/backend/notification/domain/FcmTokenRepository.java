package com.aliens.backend.notification.domain;

import com.aliens.backend.auth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    FcmToken findByMember(Member member);
}
