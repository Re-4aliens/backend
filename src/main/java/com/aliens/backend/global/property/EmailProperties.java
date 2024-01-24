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

    @Value("${smtp.protocol}")
    private String protocol;

    @Value("${smtp.auth}")
    private String auth;

    @Value("${smtp.starttls}")
    private String starttls;

    @Value("${smtp.debug}")
    private String debug;

    public String getProtocol() {
        return protocol;
    }

    public String getAuth() {
        return auth;
    }

    public String getDebug() {
        return debug;
    }

    public String getStarttls() {
        return starttls;
    }

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
