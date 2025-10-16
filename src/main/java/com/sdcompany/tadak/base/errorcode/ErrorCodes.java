package com.sdcompany.tadak.base.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCodes {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
