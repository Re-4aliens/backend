package com.aliens.backend.chat.domain;

import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection = "message")
public class Message {
    @Id
    private String id;

    private MessageType type;
    private String content;
    private Long roomId;
    private Long senderId;
    private Long receiverId;
    private Date sendTime;
    private Boolean isRead;

    protected Message() {
    }

    public static Message of(MessageSendRequest messageSendRequest){
        Message message = new Message();
        message.type = messageSendRequest.type();
        message.content = messageSendRequest.content();
        message.roomId = messageSendRequest.roomId();
        message.senderId = messageSendRequest.senderId();
        message.receiverId = messageSendRequest.receiverId();
        message.sendTime = new Date();
        message.isRead = false;
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", roomId=" + roomId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", sendTime=" + sendTime +
                ", isRead=" + isRead +
                '}';
    }
}