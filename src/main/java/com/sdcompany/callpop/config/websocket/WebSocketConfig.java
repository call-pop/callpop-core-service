package com.sdcompany.callpop.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtAuthChannelInterceptor jwtAuthChannelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 구독 엔드포인트
        config.enableSimpleBroker("/topic", "/queue");
        // 클라이언트가 보낼 prefix (MessageMapping)
        config.setApplicationDestinationPrefixes("/app");
        // 1:1 DM 같은 유저별 큐용
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // 모든 도메인 허용 todo 추후 변경
                .withSockJS();

        // todo 에러 핸들러 등록
//        registry.setErrorHandler();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtAuthChannelInterceptor);
    }
}
