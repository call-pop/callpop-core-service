package com.sdcompany.tadak.chatroom.service;

import com.sdcompany.tadak.chatroom.dto.ChatRoomResponse;
import com.sdcompany.tadak.login.dto.TadakUser;
import com.sdcompany.tadak.message.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRooms(TadakUser user) {
        return chatRoomRepository.findMyRooms(user.getId())
                .stream()
                .map(ChatRoomResponse::from)
                .toList();
    }
}
