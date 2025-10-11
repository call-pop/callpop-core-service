package com.sdcompany.tadak.chatroom.dto;

import com.sdcompany.tadak.entity.ChatRoom;

import java.time.Instant;

public record ChatRoomResponse(
        Long id,
        String name,
        String createdBy,
        Instant createdDate
) {
    public static ChatRoomResponse from(
            ChatRoom chatRoom
    ) {
        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getName(),
                chatRoom.getCreatedBy(),
                chatRoom.getCreatedDate()
        );
    }
}
