package com.aliens.backend.chat.domain.repository;

import com.aliens.backend.chat.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends MongoRepository<Message, String>, CustomMessageRepository {
}
