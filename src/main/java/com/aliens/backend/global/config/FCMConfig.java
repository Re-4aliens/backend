package com.aliens.backend.global.config;

import com.aliens.backend.global.error.CommonError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.FCMProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FCMConfig{

    private final FCMProperties fcmProperties;

    public FCMConfig(final FCMProperties fcmProperties) {
        this.fcmProperties = fcmProperties;
    }

    @PostConstruct
    public void init() {
        try {FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.
                                    fromStream(
                                            new ClassPathResource(fcmProperties.getLocationOfFcmJson()).getInputStream()
                                    )
                            )
                    .build();
            if (FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RestApiException(CommonError.FCM_CONFIGURATION_ERROR);
        }
    }
}
