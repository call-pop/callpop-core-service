package com.sdcompany.tadak.config.websocket;

import com.sdcompany.tadak.config.security.TokenProvider;
import com.sdcompany.tadak.login.dto.TadakUser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (headerAccessor == null) return message;

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            String token = firstHeader(headerAccessor, HttpHeaders.AUTHORIZATION);
            if (token == null) throw new MessageDeliveryException("No Authorization header");

            if (token.startsWith("Bearer ")) token = token.substring(7);

            // 토큰 검증 + 클레임 파싱
            Claims claims = tokenProvider.parseClaims(token);
            Long userId = Long.valueOf(claims.getSubject());
            String userIdentifier = claims.get("userIdentifier", String.class);

            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);
            if (roles == null) roles = Collections.singletonList("ROLE_USER");

            var authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            TadakUser tadakUser = new TadakUser(userId, userIdentifier, authorities);

            var authentication = new UsernamePasswordAuthenticationToken(tadakUser, null, authorities);
            headerAccessor.setUser(authentication);
        }

        if (StompCommand.SEND.equals(headerAccessor.getCommand()) && headerAccessor.getUser() == null) {
            throw new MessageDeliveryException("Unauthorized SEND");
        }
        return message;
    }

    private static String firstHeader(StompHeaderAccessor acc, String name) {
        List<String> values = acc.getNativeHeader(name);
        return (values == null || values.isEmpty()) ? null : values.get(0);
    }
}
