package com.sdcompany.callpop.message.dto;

public record ChatMessageResponse(
        String id,
        Long roomId,
        String content,
        String senderIdentifier,
        long sentAtEpochMillis
) {}
