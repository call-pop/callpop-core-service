package com.sdcompany.tadak.base.exception;

import com.sdcompany.tadak.base.errorcode.ErrorCodes;
import lombok.Getter;

import java.io.Serial;

@Getter
public class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4138351025075511080L;

    private final ErrorCodes errorCodes;
    private final Throwable cause;

    public BaseException(ErrorCodes errorCodes, Throwable cause) {
        super(cause);
        this.errorCodes = errorCodes;
        this.cause = cause;
    }

    public BaseException(ErrorCodes errorCodes) {
        this.errorCodes = errorCodes;
        this.cause = null;
    }
}
