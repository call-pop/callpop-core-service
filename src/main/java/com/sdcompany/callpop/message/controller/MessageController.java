package com.sdcompany.callpop.message.controller;

import com.sdcompany.callpop.message.dto.ChatMessageRequest;
import com.sdcompany.callpop.message.dto.ChatMessageResponse;
import com.sdcompany.callpop.message.dto.ReadReceiptEvent;
import com.sdcompany.callpop.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/rooms/{roomId}/send")
    public void send(
            @DestinationVariable Long roomId,
            ChatMessageRequest request,
            Principal principal
    ) {
        ChatMessageResponse saved = messageService.saveAndBuildResponse(roomId, request, principal);
        // 같은 방 구독자에게 방송
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, saved);
    }

    @MessageMapping("/rooms/{roomId}/read")
    public void read(
            @DestinationVariable Long roomId,
            Principal principal
    ) {
        ReadReceiptEvent event = messageService.updateLastReadAndBuildEvent(roomId, principal);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId + "/read", event);
    }
}
