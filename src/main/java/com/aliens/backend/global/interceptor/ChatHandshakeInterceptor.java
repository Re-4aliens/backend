package com.aliens.backend.global.interceptor;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.service.TokenProvider;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.service.ChatService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class ChatHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatService chatService;

    public ChatHandshakeInterceptor(TokenProvider tokenProvider, @Lazy ChatService chatService) {
        this.tokenProvider = tokenProvider;
        this.chatService = chatService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String accessToken = servletRequest.getServletRequest().getHeader("Authorization");
            LoginMember loginMember = tokenProvider.getLoginMemberFromToken(accessToken);

            if (loginMember != null) {
                List<ChatRoom> chatRooms = chatService.getChatRooms(loginMember.memberId());
                attributes.put("loginMember", loginMember);
                attributes.put("chatRooms", chatRooms);

                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}