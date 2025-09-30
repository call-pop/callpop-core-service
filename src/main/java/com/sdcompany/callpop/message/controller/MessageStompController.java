package com.sdcompany.callpop.message.controller;

import com.sdcompany.callpop.login.dto.CallPopUser;
import com.sdcompany.callpop.member.RoomMemberService;
import com.sdcompany.callpop.message.dto.ChatMessageRequest;
import com.sdcompany.callpop.message.service.MessageStompService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageStompController {

    private final MessageStompService messageStompService;
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomMemberService roomMemberService;

    @MessageMapping("/rooms/{roomId}/send")
    public void send(
            @DestinationVariable Long roomId,
            ChatMessageRequest request,
            Principal principal
    ) {
        // todo @AuthenticationPrincipal CallPopUser user 로 변경
        CallPopUser user = (CallPopUser) ((Authentication) principal).getPrincipal();
        messageStompService.saveAndBuildResponse(roomId, request, user);
    }

    @MessageMapping("/rooms/{roomId}/read")
    public void read(
            @DestinationVariable Long roomId,
            Principal principal
    ) {
        // todo @AuthenticationPrincipal CallPopUser user 로 변경
        CallPopUser user = (CallPopUser) ((Authentication) principal).getPrincipal();
        messageStompService.updateLastReadAndBuildEvent(roomId, user);
    }
}
