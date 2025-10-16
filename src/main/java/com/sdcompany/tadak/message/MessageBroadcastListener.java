package com.sdcompany.tadak.message;

import com.sdcompany.tadak.member.service.RoomMemberService;
import com.sdcompany.tadak.message.dto.MessageSavedEvent;
import com.sdcompany.tadak.message.dto.ReadUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MessageBroadcastListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomMemberService roomMemberService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(MessageSavedEvent event) {
        // 본인 제외하고 메시지 발송
        var memberIds = roomMemberService.findMemberIds(event.roomId());

        memberIds.stream()
            .filter(id -> !id.equals(event.senderId()))
            .forEach(id -> messagingTemplate.convertAndSendToUser(
                    id.toString(),
                    "/queue/rooms/" + event.roomId(),
                    event.payload()
            ));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ReadUpdatedEvent event) {
        var memberIds = roomMemberService.findMemberIds(event.roomId());

        memberIds.stream()
            .filter(id -> !id.equals(event.readerId()))
            .forEach(id -> messagingTemplate.convertAndSendToUser(
                    id.toString(),
                    "/queue/rooms/" + event.roomId() + "/read",
                    event.payload()
            ));
    }
}
