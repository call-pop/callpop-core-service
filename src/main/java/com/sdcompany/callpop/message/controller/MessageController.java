package com.sdcompany.callpop.message.controller;

import com.sdcompany.callpop.login.dto.CallPopUser;
import com.sdcompany.callpop.message.dto.ChatMessageRequest;
import com.sdcompany.callpop.message.dto.ChatMessageResponse;
import com.sdcompany.callpop.message.dto.ReadReceiptEvent;
import com.sdcompany.callpop.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/rooms/{roomId}/send")
    @SendTo("/topic/rooms/{roomId}")
    public ChatMessageResponse send(
            @DestinationVariable Long roomId,
            ChatMessageRequest request,
            Principal principal
    ) {
        // todo @AuthenticationPrincipal CallPopUser user 로 변경
        CallPopUser user = (CallPopUser) ((Authentication) principal).getPrincipal();
        ChatMessageResponse saved = messageService.saveAndBuildResponse(roomId, request, user);
        log.info("[WS] broadcast to /topic/rooms/{} :: {}", roomId, saved);
        return saved;
    }

    @MessageMapping("/rooms/{roomId}/read")
    @SendTo("/topic/rooms/{roomId}/read")
    public ReadReceiptEvent read(
            @DestinationVariable Long roomId,
            Principal principal
    ) {
        // todo @AuthenticationPrincipal CallPopUser user 로 변경
        CallPopUser user = (CallPopUser) ((Authentication) principal).getPrincipal();
        ReadReceiptEvent event = messageService.updateLastReadAndBuildEvent(roomId, user);
        log.info("[WS] broadcast to /topic/rooms/{}/read :: {}", roomId, event);
        return event;
    }
}
