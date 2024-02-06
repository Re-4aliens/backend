package com.aliens.backend.chat.domain.ChatRepository;

import com.aliens.backend.chat.domain.Message;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<Message> findMessages(Long chatRoomID, String lastMessageId) {
        Query query = new Query(
                Criteria.where("roomId").is(chatRoomID)
        ).with(Sort.by(Sort.Direction.DESC, "_id")).limit(100); // "_id"를 사용하여 정렬

        if (lastMessageId != null) {
            query.addCriteria(Criteria.where("_id").lt(new ObjectId(lastMessageId)));
        }

        return mongoTemplate.find(query, Message.class);
    }
}