package com.aliens.backend.global;

import com.aliens.backend.chat.controller.ChatController;
import com.aliens.backend.chat.domain.repository.MessageRepository;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.notification.FcmSender;
import com.aliens.backend.uploader.AwsS3Uploader;
import com.aliens.backend.uploader.dto.S3File;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @SpyBean protected AwsS3Uploader awsS3Uploader;
    @SpyBean protected JavaMailSender javaMailSender;
    @SpyBean protected FcmSender fcmSender;
    // 수정 예정
    @SpyBean protected MessageRepository messageRepository;
    @SpyBean protected ChatService chatService;
    @SpyBean protected ChatController chatController;

    @Autowired private DatabaseCleanup databaseCleanUp;

    @BeforeEach
    void setUpSpy() {
        //Data Initializer
        databaseCleanUp.execute();

        //SMTP
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        //FCM
        doNothing().when(fcmSender).listenMultiMessageRequest(any(MulticastMessage.class));
        doNothing().when(fcmSender).listenSingleMessageRequest(any(Message.class));

        //AWS
        S3File tmpFile = new S3File(DummyGenerator.GIVEN_FILE_NAME,DummyGenerator.GIVEN_FILE_URL);
        doReturn(tmpFile).when(awsS3Uploader).singleUpload(any(MultipartFile.class));
        doReturn(List.of(tmpFile)).when(awsS3Uploader).multiUpload(any());
    }
}
