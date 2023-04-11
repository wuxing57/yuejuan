package com.example.springboot.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NO_POWER("300",HttpStatus.BAD_REQUEST,"没有权限"),
    NO_Null("506",HttpStatus.BAD_REQUEST,"不能为null"),
    REPEAT_ADD("300",HttpStatus.BAD_REQUEST,"不能重复添加题目");

    private String code;
    private HttpStatus status;
    private String message;

    public String getCode() {
        return code;
    }

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "code='" + code + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
