package com.aliens.backend.docs;

import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.ChatRoomStatus;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.domain.MessageType;
import com.aliens.backend.chat.service.model.ChatMessageSummary;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatRestDocsTest extends BaseRestDocsTest {

    @Test
    @DisplayName("API - 채팅 메시지 조회")
    void getMessages() throws Exception {
        Long chatRoomId = 1L;
        MessageSendRequest request1 = new MessageSendRequest(MessageType.NORMAL, "first message", chatRoomId, 101L, 102L);
        MessageSendRequest request2 = new MessageSendRequest(MessageType.NORMAL, "Second message", chatRoomId, 101L, 102L);

        Message message1 = Message.of(request1);
        Message message2 = Message.of(request2);

        List<Message> result = Arrays.asList(message1, message2);

        SuccessResponse<List<Message>> response = SuccessResponse.of(ChatSuccess.GET_MESSAGES_SUCCESS, result);
        doReturn(response).when(chatController).getMessages(any(),any());

        mockMvc.perform(get("/chat/room/{roomId}/messages", chatRoomId)
                        .header("Authorization", GIVEN_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("chat-get-messages",
                        pathParameters(
                                parameterWithName("roomId").description("채팅방의 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("메시지 목록 조회 결과"),
                                fieldWithPath("result[].id").description("메시지 ID"),
                                fieldWithPath("result[].type").description("메시지 유형"),
                                fieldWithPath("result[].content").description("메시지 내용"),
                                fieldWithPath("result[].roomId").description("채팅방 ID"),
                                fieldWithPath("result[].senderId").description("메시지를 보낸 사용자의 ID"),
                                fieldWithPath("result[].receiverId").description("메시지를 받은 사용자의 ID"),
                                fieldWithPath("result[].sendTime").description("메시지가 보내진 시간"),
                                fieldWithPath("result[].isRead").description("메시지 읽음 상태 (true: 읽음, false: 읽지 않음)")
                        )
                ));
    }

    @Test
    @DisplayName("API - 채팅 요약 정보 조회")
    void getChatSummary() throws Exception {
        List<ChatRoom> chatRooms = new ArrayList<>();
        ChatRoom chatRoom1 = mock(ChatRoom.class);
        when(chatRoom1.getId()).thenReturn(1L);
        when(chatRoom1.getStatus()).thenReturn(ChatRoomStatus.OPEN);
        ChatRoom chatRoom2 = mock(ChatRoom.class);
        when(chatRoom2.getId()).thenReturn(2L);
        when(chatRoom2.getStatus()).thenReturn(ChatRoomStatus.OPEN);
        chatRooms.add(chatRoom1);
        chatRooms.add(chatRoom2);
        Date now = new Date();

        List<ChatMessageSummary> chatMessageSummaries = List.of(
                new ChatMessageSummary(1L, "Last message in Room 1", now, 5L),
                new ChatMessageSummary(2L, "Last message in Room 2", new Date(now.getTime() - 3600000), 3L)
        );

        ChatSummaryResponse result = new ChatSummaryResponse(chatRooms, chatMessageSummaries);

        SuccessResponse<ChatSummaryResponse> response = SuccessResponse.of(ChatSuccess.GET_SUMMARIES_SUCCESS, result);

        doReturn(response).when(chatController).getChatSummaries(any());


        mockMvc.perform(get("/chat/summaries")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(document("chat-get-summaries",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result.chatRooms").description("사용자가 속한 채팅방 목록"),
                                fieldWithPath("result.chatRooms[].id").description("채팅방의 ID"),
                                fieldWithPath("result.chatRooms[].status").description("채팅방의 상태"),
                                fieldWithPath("result.chatMessageSummaries").description("각 채팅방의 요약 정보"),
                                fieldWithPath("result.chatMessageSummaries[].roomId").description("채팅방 ID"),
                                fieldWithPath("result.chatMessageSummaries[].lastMessageContent").description("채팅방의 마지막 메시지 내용"),
                                fieldWithPath("result.chatMessageSummaries[].lastMessageTime").description("채팅방의 마지막 메시지 시간"),
                                fieldWithPath("result.chatMessageSummaries[].numberOfUnreadMessages").description("읽지 않은 메시지의 수")
                        )
                ));
    }
}