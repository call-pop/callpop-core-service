package com.sdcompany.tadak.message.repository;

import com.sdcompany.tadak.entity.ChatRoom;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
        select r
        from RoomMember rm
        join rm.chatRoom r
        where rm.user.id = :userId
        order by r.createdDate desc
    """)
    List<ChatRoom> findMyRooms(@Param("userId") Long userId);
}
