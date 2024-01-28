package com.aliens.backend.email.domain.repository;

import com.aliens.backend.email.domain.EmailAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthenticationRepository extends JpaRepository<EmailAuthentication, Long> {
    Optional<EmailAuthentication> findByEmail(String email);
}
