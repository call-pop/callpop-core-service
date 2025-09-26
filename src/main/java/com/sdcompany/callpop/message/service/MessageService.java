package com.sdcompany.callpop.message.service;

import com.sdcompany.callpop.entity.ChatMessage;
import com.sdcompany.callpop.entity.ChatRoom;
import com.sdcompany.callpop.entity.RoomMember;
import com.sdcompany.callpop.entity.Users;
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

import java.security.Principal;

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
            Principal principal
    ) {

        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();

        Users user = usersRepository.findById(Long.valueOf(principal.getName()))
                .orElseThrow();

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
            Principal principal
    ) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();

        Users user = usersRepository.findById(Long.valueOf(principal.getName()))
                .orElseThrow();

        RoomMember member = roomMemberRepository.findByChatRoomAndUser(room, user)
                        .orElseThrow();

        RoomMember.updateLastRead(member);

        return new ReadReceiptEvent(roomId);
    }
}
