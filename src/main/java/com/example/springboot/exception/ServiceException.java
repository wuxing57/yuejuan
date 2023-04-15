package com.example.springboot.exception;

import lombok.Data;
import lombok.Getter;

/**
 * 自定义异常
 */
@Data
public class ServiceException extends RuntimeException {
    private String code;
    private String msg;

    private ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMessage();
    }

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
