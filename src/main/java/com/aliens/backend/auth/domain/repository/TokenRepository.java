package com.aliens.backend.auth.domain.repository;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findByMember(Member member);

    void deleteAllByMember(Member member);
}
