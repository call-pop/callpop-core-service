package com.sdcompany.callpop.message.service;

import com.sdcompany.callpop.entity.ChatMessage;
import com.sdcompany.callpop.entity.ChatRoom;
import com.sdcompany.callpop.entity.RoomMember;
import com.sdcompany.callpop.entity.Users;
import com.sdcompany.callpop.login.dto.CallPopUser;
import com.sdcompany.callpop.message.dto.ChatMessageRequest;
import com.sdcompany.callpop.message.dto.ChatMessageResponse;
import com.sdcompany.callpop.message.dto.MessageSavedEvent;
import com.sdcompany.callpop.message.dto.ReadReceiptEvent;
import com.sdcompany.callpop.message.dto.ReadUpdatedEvent;
import com.sdcompany.callpop.message.repository.ChatMessageRepository;
import com.sdcompany.callpop.message.repository.ChatRoomRepository;
import com.sdcompany.callpop.message.repository.RoomMemberRepository;
import com.sdcompany.callpop.message.repository.UsersRepository;
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
            CallPopUser callPopUser
    ) {

        ChatRoom room = getChatRoom(roomId);
        Users user = getUser(callPopUser.getId());

        ChatMessage message = ChatMessage.create(room, user, request.content());
        ChatMessage saved = chatMessageRepository.save(message);

        ChatMessageResponse response = new ChatMessageResponse(
                saved.getId(), roomId, saved.getContent(),
                saved.getSender().getUsername(), saved.getSentAt()
        );

        eventPublisher.publishEvent(new MessageSavedEvent(roomId, callPopUser.getId(), response));
        return response;
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

        ReadReceiptEvent payload = new ReadReceiptEvent(roomId, callPopUser.getId(), Instant.now());

        eventPublisher.publishEvent(new ReadUpdatedEvent(roomId, callPopUser.getId(), payload));
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
