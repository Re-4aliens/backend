package com.aliens.backend.docs;

import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.NotificationSuccess;
import com.aliens.backend.notification.controller.dto.NotificationResponse;
import com.aliens.backend.notification.domain.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationRestDocsTest extends BaseRestDocsTest{

    @Test
    @DisplayName("API - 알림 조회")
    void getNotifications() throws Exception {
        final NotificationResponse notification = new NotificationResponse(1L,
                " 재밌어 보이네요. 라는 댓글이 달렸습니다.",
                1L,
                NotificationType.PERSONAL,
                BoardCategory.FREE,
                Instant.now(),
                false);
        SuccessResponse<List<NotificationResponse>> response = SuccessResponse.of(NotificationSuccess.GET_NOTIFICATION_SUCCESS, List.of(notification, notification));

        doReturn(response).when(notificationController).getNotifications(any());

        // When and Then
        mockMvc.perform(get("/notifications")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("notification-get",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("알림 조회 결과"),
                                fieldWithPath("result[].id").description("알림 id"),
                                fieldWithPath("result[].content").description("알림 내용"),
                                fieldWithPath("result[].boardId").description("해당 알림의 게시글 id"),
                                fieldWithPath("result[].noticeType").description("해당 알림의 타입"),
                                fieldWithPath("result[].category").description("해당 알림의 게시글 카테고리"),
                                fieldWithPath("result[].createdAt").description("알림 발생시간"),
                                fieldWithPath("result[].isRead").description("알림 읽음 여부")
                        )));
    }

    @Test
    @DisplayName("API - fcm토큰 등록")
    void registerFcmToken() throws Exception {
        SuccessResponse<String> response = SuccessResponse.of(NotificationSuccess.REGISTER_TOKEN_SUCCESS);
        String requestBody = "{\"fcmToken\": \"" + "givenToken" + "\"}";
        doReturn(response).when(notificationController).registerFcmToken(any(), any());

        // When and Then
        mockMvc.perform(post("/notifications/fcm")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("notification-register-fcm-token",
                        requestFields(
                                fieldWithPath("fcmToken").description("저장하기 위한 fcm토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("Fcm 토큰 저장 결과")
                        )));
    }

    @Test
    @DisplayName("API - 알림 읽음 요청")
    void readNotification() throws Exception {
        final int notificationId = 1;
        SuccessResponse<String> response = SuccessResponse.of(NotificationSuccess.READ_NOTIFICATION_SUCCESS);

        doReturn(response).when(notificationController).readNotification(any(), any());

        // When and Then
        mockMvc.perform(patch("/notifications?id=" + notificationId)
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("notification-read",
                        queryParameters(
                                parameterWithName("id").description("읽고자 하는 알림의 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("알림 읽음 요청 결과")
                        )));
    }
}
