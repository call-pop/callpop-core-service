package com.sdcompany.tadak.base.errorcode;

import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ResponseCode implements ErrorCodes {
    ERROR("9999", "error"),
    FEIGN_ERROR("9998", "feign.error"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "9997", "invalid.request"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "9996", "invalid.parameter"),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "9995", "authentication.failed"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "9994", "access.denied"),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "9993", "illegal.argument"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "9992", "login.failed"),

    ;

    public final HttpStatus httpStatus;
    public final String code;
    public final String message;

    private static final Map<String, ResponseCode> BY_MESSAGE =
            Stream.of(values())
                    .collect(Collectors.toMap(ResponseCode::getMessage, Function.identity()));

    ResponseCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    ResponseCode(String code, String message) {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public static ResponseCode findByMessage(String message) {
        return BY_MESSAGE.get(message);
    }
}
