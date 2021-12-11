package com.esecondhand.esecondhand.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private HandShakeInterceptor handShakeInterceptor;

    public WebSocketConfig(HandShakeInterceptor handShakeInterceptor) {
        this.handShakeInterceptor = handShakeInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/ws");
        config.enableSimpleBroker("/topic");

//        registry.enableSimpleBroker("/queue", "/topic");
//        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat").addInterceptors(handShakeInterceptor).setAllowedOriginPatterns("*").withSockJS();
//        registry.addEndpoint("/messenger", "/call").addInterceptors(handShakeInterceptor).setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/chat").addInterceptors(handShakeInterceptor).setAllowedOrigins("http://localhost:3000").withSockJS().setWebSocketEnabled(true);
//        registry.addEndpoint("/messenger", "/call").setAllowedOriginPatterns();

    }
}
