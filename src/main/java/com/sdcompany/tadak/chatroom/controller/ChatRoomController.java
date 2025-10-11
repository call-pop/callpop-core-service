package com.sdcompany.tadak.chatroom.controller;

import com.sdcompany.tadak.chatroom.dto.ChatRoomResponse;
import com.sdcompany.tadak.chatroom.service.ChatRoomService;
import com.sdcompany.tadak.login.dto.TadakUser;
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
            @AuthenticationPrincipal TadakUser user
    ) {
        return chatRoomService.getChatRooms(user);
    }
}
