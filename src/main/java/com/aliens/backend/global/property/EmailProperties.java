package com.aliens.backend.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class EmailProperties {

    @Value("${domain.url}")
    private String domainUrl;

    @Value("${smtp.email}")
    private String email;

    @Value("${smtp.password}")
    private String password;

    @Value("${smtp.host}")
    private String host;

    @Value("${smtp.port}")
    private int port;

    public String getDomainUrl() {
        return domainUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
