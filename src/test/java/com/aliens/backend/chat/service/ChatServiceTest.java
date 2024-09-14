package com.aliens.backend.chat.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.MessageType;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.domain.model.MemberPair;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


class ChatServiceTest extends BaseIntegrationTest {

    @Autowired ChatService chatService;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired DummyGenerator dummyGenerator;

    Member receiver;
    Member sender;
    ChatRoom givenChatRoom;
    Message givenMessage;

    @BeforeEach
    void setUp() {
        messageRepository.deleteAll();
        setChatRoom();
    }

    private void setChatRoom() {
        receiver = dummyGenerator.generateSingleMember();
        sender = dummyGenerator.generateSingleMember();

        MemberPair receiverMemberPair = new MemberPair(receiver, sender);
        givenChatRoom = ChatRoom.createOpenChatroom(receiverMemberPair);
        givenChatRoom = chatRoomRepository.save(givenChatRoom);
    }

    @Test
    @DisplayName("메시지 전송")
    void sendMessage() {
        //Given
        MessageSendRequest request = makeMessageSendRequest();

        //When
        String result = chatService.sendMessage(request);

        //Then
        Message savedMessage = messageRepository.findAll().get(0);
        Assertions.assertFalse(savedMessage.getIsRead());
        Assertions.assertEquals(ChatSuccess.SEND_MESSAGE_SUCCESS.getMessage(), result);
    }

    @Test
    @DisplayName("메시지 읽음 처리")
    void readMessages() {
        //Given
        savedMessages();
        ReadRequest readRequest = new ReadRequest(givenChatRoom.getId(), receiver.getId());
        String expectedResponse = ChatSuccess.READ_MESSAGES_SUCCESS.getMessage();

        //When
        String result = chatService.readMessages(readRequest);

        //Then
        Message isReadMessage = messageRepository.findAll().get(0);
        Assertions.assertTrue(isReadMessage.getIsRead());
        Assertions.assertEquals(expectedResponse, result);
    }

    private void savedMessages() {
        MessageSendRequest request = makeMessageSendRequest();
        givenMessage = Message.from(request);
        givenMessage = messageRepository.save(givenMessage);
    }

    @Test
    @DisplayName("채팅 요약 정보 조회 - 수신 측")
    void getReceiverChatSummary() {
        //Given
        savedMessages();
        LoginMember loginMember = receiver.getLoginMember();

        //When
        ChatSummaryResponse result = chatService.getChatSummaries(loginMember);

        //Then
        Assertions.assertEquals(1, result.chatMessageSummaries().size());
        Assertions.assertEquals(1, result.chatMessageSummaries().get(0).numberOfUnreadMessages());
        Assertions.assertEquals(result.chatMessageSummaries().get(0).lastMessageContent(), "안녕하세요.");
        Assertions.assertEquals(result.chatRooms().size(), 1);
    }

    @Test
    @DisplayName("채팅 요약 정보 조회 - 발신 측")
    void getSenderChatSummary() {
        //Given
        savedMessages();
        LoginMember loginMember = sender.getLoginMember();

        //When
        ChatSummaryResponse result = chatService.getChatSummaries(loginMember);

        //Then
        Assertions.assertEquals(1,result.chatMessageSummaries().size());
        Assertions.assertEquals(0, result.chatMessageSummaries().get(0).numberOfUnreadMessages());
        Assertions.assertEquals(result.chatMessageSummaries().get(0).lastMessageContent(), "안녕하세요.");
        Assertions.assertEquals(result.chatRooms().size(), 1);
    }

    @Test
    @DisplayName("메시지 조회")
    void getMessages() {
        //Given
        savedMessages();
        Long chatRoomId = 1L;

        //When
        List<Message> result = chatService.getMessages(chatRoomId, null);

        //Then
        Assertions.assertNotNull(result);
    }

    private MessageSendRequest makeMessageSendRequest() {
        return new MessageSendRequest(MessageType.NORMAL,
                "안녕하세요.",
                givenChatRoom.getId(),
                sender.getId(),
                receiver.getId());
    }
}