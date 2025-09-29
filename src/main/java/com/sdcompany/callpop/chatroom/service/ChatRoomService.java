package com.sdcompany.callpop.chatroom.service;

import com.sdcompany.callpop.chatroom.dto.ChatRoomResponse;
import com.sdcompany.callpop.login.dto.CallPopUser;
import com.sdcompany.callpop.message.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRooms(CallPopUser user) {
        return chatRoomRepository.findMyRooms(user.getId())
                .stream()
                .map(ChatRoomResponse::from)
                .toList();
    }
}
