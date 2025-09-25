package com.sdcompany.callpop.message.dto;

public record ChatMessageRequest(
        String roomId,
        String content,
        String senderId
) {}