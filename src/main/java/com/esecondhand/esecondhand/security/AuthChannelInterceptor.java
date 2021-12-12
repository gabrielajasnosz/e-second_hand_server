package com.esecondhand.esecondhand.security;

import com.esecondhand.esecondhand.service.UserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;

    private UserService userService;

    private AuthenticationManager authenticationManager;

    public AuthChannelInterceptor(JwtTokenUtil jwtTokenUtil, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (accessor.getCommand() == StompCommand.CONNECT || accessor.getCommand() == StompCommand.SEND) {

            final String token = accessor.getFirstNativeHeader("x-auth-token");

            String username = jwtTokenUtil.getUsernameFromToken(token);


            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                accessor.setUser(usernamePasswordAuthenticationToken);

            }
        }

        return message;

    }

}
