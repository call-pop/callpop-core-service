package com.sdcompany.callpop.message.dto;

public record ReadReceiptEvent(
        Long roomId,
        String readerIdentifier,
        long readAtEpochMillis
) {}