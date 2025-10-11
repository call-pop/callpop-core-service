package com.sdcompany.tadak.message.repository;

import com.sdcompany.tadak.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
