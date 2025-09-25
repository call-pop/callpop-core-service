package com.sdcompany.callpop.message.dto;

public record ReadReceiptRequest(
        String roomId,
        String readerId,
        long readAtEpochMillis
) {}