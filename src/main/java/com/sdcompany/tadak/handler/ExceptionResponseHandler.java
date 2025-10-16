package com.sdcompany.tadak.handler;

import com.sdcompany.tadak.base.dto.ErrorResponse;
import com.sdcompany.tadak.base.errorcode.ResponseCode;
import com.sdcompany.tadak.base.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ExceptionResponseHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        log.error("Unexpected exception occurred : ", exception);
        BaseException baseException = new BaseException(ResponseCode.ERROR, exception);
        return generateErrorResponse(baseException);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException exception) {
        log.error("BaseException occurred : errorCodes : {}, message : {}",
                exception.getErrorCodes().getCode(), exception.getErrorCodes().getMessage());

        return generateErrorResponse(exception);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadableException(HttpMessageNotReadableException e) {
        Throwable throwable = e.getMostSpecificCause();
        if (throwable instanceof IllegalArgumentException exception) {
            return illegalArgumentExceptionHandler(exception);
        }
        log.error("HttpMessageNotReadableException occurred : ", e);
        BaseException baseException = new BaseException(ResponseCode.INVALID_PARAMETER, e);
        return generateErrorResponse(baseException);
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(
            IllegalArgumentException invalidDataAccessApiUsageException) {
        log.error("IllegalArgumentException occurred : ", invalidDataAccessApiUsageException);
        String message = invalidDataAccessApiUsageException.getMessage();
        BaseException baseException = new BaseException(ResponseCode.findByMessage(message));
        if (baseException.getErrorCodes() == null) {
            baseException = new BaseException(ResponseCode.ILLEGAL_ARGUMENT, invalidDataAccessApiUsageException);
        }
        return generateErrorResponse(baseException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException occurred: ", e);

        return ResponseEntity.status(ResponseCode.INVALID_PARAMETER.getHttpStatus())
                .body(
                        new ErrorResponse(
                                ResponseCode.INVALID_PARAMETER.getHttpStatus(),
                                ResponseCode.INVALID_PARAMETER.getCode(),
                                e.getBindingResult().getAllErrors().getFirst().getDefaultMessage()
                        )
                );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error("AuthenticationException occurred : ", e);
        BaseException baseException = new BaseException(ResponseCode.AUTHENTICATION_FAILED, e);
        return generateErrorResponse(baseException);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error("AuthorizationDeniedException occurred : ", e);
        BaseException baseException = new BaseException(ResponseCode.ACCESS_DENIED, e);
        return generateErrorResponse(baseException);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException occurred : ", e);
        BaseException baseException = new BaseException(ResponseCode.INVALID_PARAMETER, e);
        return generateErrorResponse(baseException);
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(BaseException exception) {
        return ResponseEntity.status(exception.getErrorCodes().getHttpStatus())
                .body(
                        new ErrorResponse(
                                exception.getErrorCodes().getHttpStatus(),
                                exception.getErrorCodes().getCode(),
                                exception.getMessage()
                        )
                );
    }
}
