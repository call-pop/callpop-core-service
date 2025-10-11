package com.sdcompany.tadak.message.dto;

import com.sdcompany.tadak.utils.DateFormat;

import java.time.Instant;

public record ChatMessageResponse(
        Long id,
        Long roomId,
        String content,
        String senderName,
        @DateFormat
        Instant sentAt
) {}
