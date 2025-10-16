package com.sdcompany.tadak.in.chatroom;

import com.sdcompany.tadak.chatroom.dto.ChatRoomResponse;
import com.sdcompany.tadak.chatroom.service.ChatRoomService;
import com.sdcompany.tadak.login.dto.TadakUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ChatRoom", description = "채팅방 API")
@RequestMapping("/api/chatrooms")
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(
            summary = "채팅방 목록 조회",
            description = "사용자가 속한 채팅방 목록을 조회합니다."
    )
    @GetMapping("/list")
    public List<ChatRoomResponse> getChatRooms(
            @AuthenticationPrincipal TadakUser user
    ) {
        return chatRoomService.getChatRooms(user);
    }
}
