package com.sdcompany.callpop.message.dto;

public record MessageSavedEvent(
    Long roomId,
    Long senderId,
    ChatMessageResponse payload
) {}
