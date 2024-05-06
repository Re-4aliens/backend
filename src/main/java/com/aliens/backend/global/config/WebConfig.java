package com.aliens.backend.global.config;

import com.aliens.backend.global.config.resolver.TokenInfoResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenInfoResolver tokenInfoResolver;

    public WebConfig(final TokenInfoResolver tokenInfoResolver) {
        this.tokenInfoResolver = tokenInfoResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(tokenInfoResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/public/images/**").addResourceLocations("classpath:/static/public/images/");
    }
}
