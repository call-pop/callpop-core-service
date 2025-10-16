package com.sdcompany.tadak.in.stomp;

import com.sdcompany.tadak.login.dto.TadakUser;
import com.sdcompany.tadak.message.dto.ChatMessageRequest;
import com.sdcompany.tadak.message.service.MessageStompService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageStompController {

    private final MessageStompService messageStompService;

    @MessageMapping("/rooms/{roomId}/send")
    public void send(
            @DestinationVariable Long roomId,
            ChatMessageRequest request,
            Principal principal
    ) {
        // todo @AuthenticationPrincipal Tadak user 로 변경
        TadakUser user = (TadakUser) ((Authentication) principal).getPrincipal();
        messageStompService.saveAndBuildResponse(roomId, request, user);
    }

    @MessageMapping("/rooms/{roomId}/read")
    public void read(
            @DestinationVariable Long roomId,
            Principal principal
    ) {
        // todo @AuthenticationPrincipal Tadak user 로 변경
        TadakUser user = (TadakUser) ((Authentication) principal).getPrincipal();
        messageStompService.updateLastReadAndBuildEvent(roomId, user);
    }
}
