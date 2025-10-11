package com.sdcompany.tadak.message.dto;

public record ReadUpdatedEvent(
    Long roomId,
    Long readerId,
    ReadReceiptEvent payload
) {}
