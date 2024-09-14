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

    public static Message from(MessageSendRequest messageSendRequest){
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

    public String getId() {
        return id;
    }

    public MessageType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public Boolean getIsRead() {
        return isRead;
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