package com.aliens.backend.chat.socket;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class ChatClientHandler extends StompSessionHandlerAdapter {
        public ChatClientHandler() {}

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {}

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {}
 }