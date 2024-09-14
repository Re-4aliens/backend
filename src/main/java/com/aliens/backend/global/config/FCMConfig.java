package com.aliens.backend.global.config;

import com.aliens.backend.global.response.error.CommonError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.FCMProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FCMConfig{

    private final FCMProperties fcmProperties;

    public FCMConfig(final FCMProperties fcmProperties) {
        this.fcmProperties = fcmProperties;
    }

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = new ClassPathResource(fcmProperties.getLocationOfFcmJson()).getInputStream();

            FirebaseOptions options = FirebaseOptions.builder().
                    setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RestApiException(CommonError.FCM_CONFIGURATION_ERROR);
        }
    }
}