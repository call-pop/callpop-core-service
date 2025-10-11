package com.sdcompany.tadak.member;

import com.sdcompany.tadak.entity.ChatRoom;
import com.sdcompany.tadak.message.repository.ChatRoomRepository;
import com.sdcompany.tadak.message.repository.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomMemberService {
    private final RoomMemberRepository roomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;

    public List<Long> findMemberIds(Long roomId) {
        var chatRoom = getChatRoom(roomId);

        return roomMemberRepository.findByChatRoom(chatRoom).stream()
                .map(member -> member.getUser().getId())
                .toList();
    }

    private ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid roomId: " + roomId));
    }
}
