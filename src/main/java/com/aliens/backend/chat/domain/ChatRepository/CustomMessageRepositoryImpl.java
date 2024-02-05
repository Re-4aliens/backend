package com.aliens.backend.chat.domain.ChatRepository;

import com.aliens.backend.chat.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomMessageRepositoryImpl implements CustomMessageRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomMessageRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void markMessagesAsRead(Long chatRoomId, Long readBy) {
        Query query = new Query(Criteria.where("roomId").is(chatRoomId)
                                        .and("receiverId").is(readBy)
                                        .and("isRead").is(false));
        Update update = new Update();
        update.set("isRead", true);
        mongoTemplate.updateMulti(query, update, Message.class);
    }
}