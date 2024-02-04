package com.aliens.backend.chat.domain.ChatRepository;

import com.aliens.backend.chat.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}
