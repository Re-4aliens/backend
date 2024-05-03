package com.aliens.backend.chat.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ChatRoomTest {

    @Test
    @DisplayName("ChatRoom 생성 테스트")
    public void CreateChatRoom() {
        // given
        ChatParticipant participant1 = mock(ChatParticipant.class);
        ChatParticipant participant2 = mock(ChatParticipant.class);

        // when
        ChatRoom chatRoom = new ChatRoom(participant1, participant2);

        // then
        assertNotNull(chatRoom);
        assertEquals(ChatRoomStatus.WAITING, chatRoom.getStatus());
    }

    @Test
    @DisplayName("ChatRoom id 반환 테스트")
    public void GetId() throws NoSuchFieldException, IllegalAccessException {
        // given
        Long givenId = 1L;
        ChatRoom chatRoom = new ChatRoom();

        Field idField = ChatRoom.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(chatRoom, givenId);

        // when
        Long actualId = chatRoom.getId();

        // then
        assertEquals(givenId, actualId);
    }

    @Test
    @DisplayName("ChatRoom 차단 테스트")
    public void Block() {
        // given
        ChatParticipant participant1 = mock(ChatParticipant.class);
        ChatParticipant participant2 = mock(ChatParticipant.class);
        ChatRoom chatRoom = new ChatRoom(participant1, participant2);

        // when
        chatRoom.block();

        // then
        assertEquals(ChatRoomStatus.BLOCKED, chatRoom.getStatus());
    }
}