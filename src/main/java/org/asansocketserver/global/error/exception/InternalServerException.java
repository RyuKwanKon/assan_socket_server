package org.asansocketserver.global.error.exception;

import org.asansocketserver.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}