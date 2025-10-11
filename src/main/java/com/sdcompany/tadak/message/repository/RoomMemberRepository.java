package com.sdcompany.tadak.message.repository;

import com.sdcompany.tadak.entity.ChatRoom;
import com.sdcompany.tadak.entity.RoomMember;
import com.sdcompany.tadak.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    Optional<RoomMember> findByChatRoomAndUser(ChatRoom room, Users user);

    List<RoomMember> findByChatRoom(ChatRoom room);
}
