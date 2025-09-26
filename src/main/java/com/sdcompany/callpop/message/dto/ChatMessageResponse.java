package com.sdcompany.callpop.message.dto;

public record ChatMessageResponse(
        Long id,
        Long roomId,
        String content,
        String senderIdentifier,
        long sentAtEpochMillis
) {}
