package com.sdcompany.tadak.base.exception;


import com.sdcompany.tadak.base.errorcode.ErrorCodes;

import java.io.Serial;

public class BusinessException extends BaseException {
    @Serial
    private static final long serialVersionUID = -4351738626216682514L;

    public BusinessException(ErrorCodes errorCodes) {
        super(errorCodes);
    }
}
