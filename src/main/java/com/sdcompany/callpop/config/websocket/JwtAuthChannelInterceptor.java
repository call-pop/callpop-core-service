package com.sdcompany.callpop.config.websocket;

import com.sdcompany.callpop.config.security.TokenProvider;
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

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (headerAccessor == null) return message;

        // CONNECT 때 Authorization 헤더에서 토큰 추출/검증
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            String token = firstHeader(headerAccessor, HttpHeaders.AUTHORIZATION);
            if (token == null) throw new MessageDeliveryException("No Authorization header");

            if (token.startsWith("Bearer ")) token = token.substring(7);

            if (token != null && !token.isBlank()) {
                String subject = tokenProvider.validateTokenAndGetSubject(token); // 실패 시 예외 발생 → 연결 거부
                var auth = new UsernamePasswordAuthenticationToken(
                        subject, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                headerAccessor.setUser(auth);
            }
        }

        // SEND 시에도 인증 여부 체크
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
