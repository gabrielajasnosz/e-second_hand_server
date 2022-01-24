package com.esecondhand.esecondhand.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(final MessageSecurityMetadataSourceRegistry messages) {
        messages.simpDestMatchers("/ws/**").permitAll().anyMessage().authenticated();
        messages.simpDestMatchers("/wss/**").permitAll().anyMessage().authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
