package com.sdcompany.callpop.message.dto;

public record ReadUpdatedEvent(
    Long roomId,
    Long readerId,
    ReadReceiptEvent payload
) {}
