package com.sdcompany.callpop.message.service;

import com.sdcompany.callpop.entity.ChatMessage;
import com.sdcompany.callpop.entity.ChatRoom;
import com.sdcompany.callpop.entity.RoomMember;
import com.sdcompany.callpop.entity.Users;
import com.sdcompany.callpop.login.dto.CallPopUser;
import com.sdcompany.callpop.message.dto.ChatMessageRequest;
import com.sdcompany.callpop.message.dto.ChatMessageResponse;
import com.sdcompany.callpop.message.dto.ReadReceiptEvent;
import com.sdcompany.callpop.message.repository.ChatMessageRepository;
import com.sdcompany.callpop.message.repository.ChatRoomRepository;
import com.sdcompany.callpop.message.repository.RoomMemberRepository;
import com.sdcompany.callpop.message.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public ChatMessageResponse saveAndBuildResponse(
            Long roomId,
            ChatMessageRequest request,
            CallPopUser principal
    ) {

        ChatRoom room = getChatRoom(roomId);
        Users user = getUser(principal.getId());

        ChatMessage message = ChatMessage.create(
                room, user, request.content()
        );

        ChatMessage saved = chatMessageRepository.save(message);

        return new ChatMessageResponse(
                saved.getId(),
                roomId,
                saved.getContent(),
                saved.getSender().getUserIdentifier(),
                saved.getSentAt().toEpochMilli()
        );
    }

    @Transactional
    public ReadReceiptEvent updateLastReadAndBuildEvent(
            Long roomId,
            CallPopUser callPopUser
    ) {
        ChatRoom room = getChatRoom(roomId);
        Users user = getUser(callPopUser.getId());
        RoomMember member = getMember(room, user);

        RoomMember.updateLastRead(member);

        return new ReadReceiptEvent(roomId);
    }

    private RoomMember getMember(ChatRoom room, Users user) {
        return roomMemberRepository.findByChatRoomAndUser(room, user)
                .orElseThrow();
    }

    private ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow();
    }

    private Users getUser(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow();
    }
}
