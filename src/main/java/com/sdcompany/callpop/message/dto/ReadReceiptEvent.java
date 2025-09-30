package com.sdcompany.callpop.message.dto;

import java.time.Instant;

public record ReadReceiptEvent(
        Long roomId,
        Long readerId,
        Instant readAt
) {}
