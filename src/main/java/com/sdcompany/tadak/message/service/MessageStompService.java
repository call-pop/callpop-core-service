package com.sdcompany.tadak.message.service;

import com.sdcompany.tadak.entity.ChatMessage;
import com.sdcompany.tadak.entity.ChatRoom;
import com.sdcompany.tadak.entity.RoomMember;
import com.sdcompany.tadak.entity.Users;
import com.sdcompany.tadak.login.dto.TadakUser;
import com.sdcompany.tadak.message.dto.ChatMessageRequest;
import com.sdcompany.tadak.message.dto.ChatMessageResponse;
import com.sdcompany.tadak.message.dto.MessageSavedEvent;
import com.sdcompany.tadak.message.dto.ReadReceiptEvent;
import com.sdcompany.tadak.message.dto.ReadUpdatedEvent;
import com.sdcompany.tadak.message.repository.ChatMessageRepository;
import com.sdcompany.tadak.message.repository.ChatRoomRepository;
import com.sdcompany.tadak.message.repository.RoomMemberRepository;
import com.sdcompany.tadak.message.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MessageStompService {
    private final ChatMessageRepository chatMessageRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ChatMessageResponse saveAndBuildResponse(
            Long roomId,
            ChatMessageRequest request,
            TadakUser tadakUser
    ) {

        ChatRoom room = getChatRoom(roomId);
        Users user = getUser(tadakUser.getId());

        ChatMessage message = ChatMessage.create(room, user, request.content());
        ChatMessage saved = chatMessageRepository.save(message);

        ChatMessageResponse response = new ChatMessageResponse(
                saved.getId(), roomId, saved.getContent(),
                saved.getSender().getUsername(), saved.getSentAt()
        );

        eventPublisher.publishEvent(new MessageSavedEvent(roomId, tadakUser.getId(), response));
        return response;
    }

    @Transactional
    public ReadReceiptEvent updateLastReadAndBuildEvent(
            Long roomId,
            TadakUser tadakUser
    ) {
        ChatRoom room = getChatRoom(roomId);
        Users user = getUser(tadakUser.getId());
        RoomMember member = getMember(room, user);

        RoomMember.updateLastRead(member);

        ReadReceiptEvent payload = new ReadReceiptEvent(roomId, tadakUser.getId(), Instant.now());

        eventPublisher.publishEvent(new ReadUpdatedEvent(roomId, tadakUser.getId(), payload));
        return payload;
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
