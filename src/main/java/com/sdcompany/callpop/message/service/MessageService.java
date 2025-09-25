package com.sdcompany.callpop.message.service;

import com.sdcompany.callpop.entity.ChatMessage;
import com.sdcompany.callpop.entity.ChatRoom;
import com.sdcompany.callpop.entity.RoomMember;
import com.sdcompany.callpop.message.dto.ChatMessageRequest;
import com.sdcompany.callpop.message.dto.ChatMessageResponse;
import com.sdcompany.callpop.message.dto.ReadReceiptEvent;
import com.sdcompany.callpop.message.dto.ReadReceiptRequest;
import com.sdcompany.callpop.message.repository.ChatMessageRepository;
import com.sdcompany.callpop.message.repository.ChatRoomRepository;
import com.sdcompany.callpop.message.repository.RoomMemberRepository;
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

    public ChatMessageResponse saveAndBuildResponse(
            Long roomId,
            ChatMessageRequest req,
            Principal principal
    ) {

        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();

        String senderId = (principal != null) ? principal.getName() : req.senderId();

        ChatMessage message = ChatMessage.create(
                room,
                senderId,
                req.content()
        );

        return new ChatMessageResponse(
                message.getId().toString(), roomId, message.getContent(), message.getSenderId(), message.getSentAt().toEpochMilli()
        );
    }

    @Transactional
    public ReadReceiptEvent updateLastReadAndBuildEvent(
            Long roomId,
            ReadReceiptRequest req,
            Principal principal
    ) {
        String reader = (principal != null) ? principal.getName() : req.readerId();

        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();

        RoomMember member = roomMemberRepository.findByChatRoomAndUserId(room, reader)
                        .orElseThrow();

        RoomMember.updateLastRead(member, req.readAtEpochMillis());

        return new ReadReceiptEvent(roomId, reader, req.readAtEpochMillis());
    }
}
