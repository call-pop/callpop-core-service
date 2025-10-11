package com.sdcompany.tadak.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 방 이름 (1:1 방이면 null or 유저 조합으로도 가능)

    @Column(nullable = false)
    private String createdBy; // 방 만든 사람 userId

    @Column(nullable = false, updatable = false)
    private Instant createdDate = Instant.now();

    /**
     * 양방향 매핑 - 방 ↔ 메시지
     * 메시지는 방에 소속되므로 ManyToOne(메시지 → 방) 기준
     */
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    /**
     * 방 ↔ 멤버 (RoomMember가 조인 테이블 성격)
     */
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomMember> members = new ArrayList<>();
}
