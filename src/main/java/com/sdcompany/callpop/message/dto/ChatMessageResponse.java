package com.sdcompany.callpop.message.dto;

import com.sdcompany.callpop.utils.DateFormat;

import java.time.Instant;

public record ChatMessageResponse(
        Long id,
        Long roomId,
        String content,
        String senderName,
        @DateFormat
        Instant sentAt
) {}
