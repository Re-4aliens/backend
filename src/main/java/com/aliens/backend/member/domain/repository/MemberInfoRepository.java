package com.aliens.backend.member.domain.repository;

import com.aliens.backend.member.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
}
