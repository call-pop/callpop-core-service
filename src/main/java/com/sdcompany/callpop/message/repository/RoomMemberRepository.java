package com.sdcompany.callpop.message.repository;

import com.sdcompany.callpop.entity.ChatRoom;
import com.sdcompany.callpop.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    Optional<RoomMember> findByChatRoomAndUserId(ChatRoom chatRoom, String userId);
}
