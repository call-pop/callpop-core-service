package com.sdcompany.tadak.base.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        HttpStatus status,
        String code,
        String message
) {
}
