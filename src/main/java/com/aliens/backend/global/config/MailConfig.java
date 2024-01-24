package com.aliens.backend.global.config;

import com.aliens.backend.global.property.EmailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    private final EmailProperties emailProperties;

    public MailConfig(final EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl jms = new JavaMailSenderImpl();
        jms.setHost(emailProperties.getHost());
        jms.setPort(emailProperties.getPort());
        jms.setUsername(emailProperties.getEmail());
        jms.setPassword(emailProperties.getPassword());
        jms.setJavaMailProperties(getMailProperties());
        return jms;
    }

    private Properties getMailProperties() {
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", emailProperties.getProtocol());
        prop.setProperty("mail.smtp.auth", emailProperties.getAuth());
        prop.setProperty("mail.smtp.starttls.enable", emailProperties.getStarttls());
        prop.setProperty("mail.debug",emailProperties.getDebug());
        return prop;
    }
}