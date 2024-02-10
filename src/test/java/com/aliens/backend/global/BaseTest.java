package com.aliens.backend.global;

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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public abstract class BaseTest {

    @SpyBean
    protected AwsS3Uploader awsS3Uploader;

    @SpyBean
    protected JavaMailSender javaMailSender;

    @SpyBean
    protected FcmSender fcmSender;

    @Autowired
    private DatabaseCleanup databaseCleanUp;

    private static final String GIVEN_FILE_NAME = "test";
    private static final String GIVEN_FILE_URL = "/test";

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
        S3File tmpFile = new S3File(GIVEN_FILE_NAME, GIVEN_FILE_URL);
        doReturn(tmpFile).when(awsS3Uploader).singleUpload(any(MultipartFile.class));
        doReturn(List.of(tmpFile)).when(awsS3Uploader).multiUpload(any());
    }
}
