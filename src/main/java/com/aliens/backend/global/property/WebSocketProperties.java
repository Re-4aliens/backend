package com.aliens.backend.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebSocketProperties {

    @Value("${websocket.port}")
    private String port;

    @Value("${websocket.request}")
    private String appDestinationPrefix;

    @Value("${websocket.endpoint}")
    private String endpoint;

    public String getPort() {
        return port;
    }

    public String getAppDestinationPrefix() {
        return appDestinationPrefix;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
