package com.aliens.backend.block.domain.repository;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.domain.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findAllByBlockingMember(Member blockingMember);
}
