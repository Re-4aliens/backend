package com.aliens.backend.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FCMProperties {

    @Value("${fcm.certification}")
    String locationOfFcmJson;

    public String getLocationOfFcmJson() {
        return locationOfFcmJson;
    }
}
