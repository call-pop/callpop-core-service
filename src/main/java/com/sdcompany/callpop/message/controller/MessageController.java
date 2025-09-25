package com.sdcompany.callpop.message.controller;

import com.sdcompany.callpop.message.dto.ChatMessageRequest;
import com.sdcompany.callpop.message.dto.ChatMessageResponse;
import com.sdcompany.callpop.message.dto.ReadReceiptEvent;
import com.sdcompany.callpop.message.dto.ReadReceiptRequest;
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

    // 메시지 전송
    @MessageMapping("/rooms/{roomId}/send")
    public void send(
            @DestinationVariable Long roomId,
            ChatMessageRequest req,
            Principal principal // 인증 시 이거로 senderId 대체 가능
    ) {
        ChatMessageResponse saved = messageService.saveAndBuildResponse(roomId, req, principal);
        // 같은 방 구독자에게 방송
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, saved);
    }

    // 읽음 이벤트
    @MessageMapping("/rooms/{roomId}/read")
    public void read(
            @DestinationVariable Long roomId,
            ReadReceiptRequest req,
            Principal principal
    ) {
        ReadReceiptEvent event = messageService.updateLastReadAndBuildEvent(roomId, req, principal);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId + "/read", event);
    }
}
