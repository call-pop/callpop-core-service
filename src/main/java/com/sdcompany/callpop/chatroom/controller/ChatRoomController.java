package com.sdcompany.callpop.chatroom.controller;

import com.sdcompany.callpop.chatroom.dto.ChatRoomResponse;
import com.sdcompany.callpop.chatroom.service.ChatRoomService;
import com.sdcompany.callpop.login.dto.CallPopUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/chatrooms")
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/list")
    public List<ChatRoomResponse> getChatRooms(
            @AuthenticationPrincipal CallPopUser user
    ) {
        return chatRoomService.getChatRooms(user);
    }
}
