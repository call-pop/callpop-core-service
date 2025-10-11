package com.sdcompany.tadak.message.dto;

public record MessageSavedEvent(
    Long roomId,
    Long senderId,
    ChatMessageResponse payload
) {}
