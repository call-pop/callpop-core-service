package com.sdcompany.callpop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어느 방의 멤버인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String userId; // 유저 고유 ID

    // 이 멤버가 마지막으로 읽은 메시지 시각
    @Column(nullable = false)
    private Instant lastReadAt = Instant.EPOCH;

    public static void updateLastRead(
            RoomMember member,
            long readAtEpochMillis
    ) {
        member.setLastReadAt(Instant.ofEpochMilli(readAtEpochMillis));
    }
}
