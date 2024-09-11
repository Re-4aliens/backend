package com.aliens.backend.chat.domain.repository;

import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.domain.model.ChatMessageSummary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        ).with(Sort.by(Sort.Direction.DESC, "_id")).limit(100);

        if (lastMessageId != null) {
            query.addCriteria(Criteria.where("_id").lt(new ObjectId(lastMessageId)));
        }

        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<ChatMessageSummary> aggregateMessageSummaries(List<Long> roomIds, Long memberId) {
        Criteria matchCriteria = Criteria.where("roomId").in(roomIds);
        AggregationOperation match = Aggregation.match(matchCriteria);

        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "sendTime");

        AggregationOperation group = Aggregation.group("roomId")
                .first("roomId").as("roomId")
                .first("content").as("lastMessageContent")
                .first("sendTime").as("lastMessageTime")
                .sum(ConditionalOperators
                        .when(new Criteria().andOperator(
                                Criteria.where("receiverId").is(memberId),
                                Criteria.where("isRead").is(false)
                        ))
                        .then(1)
                        .otherwise(0))
                .as("numberOfUnreadMessages");

        Aggregation aggregation = Aggregation.newAggregation(match, sort, group);

        AggregationResults<ChatMessageSummary> results = mongoTemplate.aggregate(
                aggregation, "message", ChatMessageSummary.class);
        return new ArrayList<>(results.getMappedResults());
    }
}