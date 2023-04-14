package com.example.springboot.exception;

import lombok.Data;
import lombok.Getter;

/**
 * 自定义异常
 */
@Data
public class ServiceException extends RuntimeException {
    private String code;

    private ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }
}
