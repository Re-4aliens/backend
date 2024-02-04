package com.aliens.backend.chat.domain;

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

    public static class MessageBuilder {
        private MessageType type;
        private String content;
        private Long roomId;
        private Long senderId;
        private Long receiverId;

        public MessageBuilder type(MessageType type) {
            this.type = type;
            return this;
        }

        public MessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public MessageBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public MessageBuilder senderId(Long senderId) {
            this.senderId = senderId;
            return this;
        }

        public MessageBuilder receiverId(Long receiverId) {
            this.receiverId = receiverId;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    private Message(MessageBuilder builder) {
        this.type = builder.type;
        this.content = builder.content;
        this.roomId = builder.roomId;
        this.senderId = builder.senderId;
        this.receiverId = builder.receiverId;
        this.sendTime = new Date();
        this.isRead = false;
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