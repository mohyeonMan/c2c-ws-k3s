package com.c2c.ws.common.exception;

public enum ErrorCode {
    WS_SESSION_CLOSED("WS-SESSION-001", "세션이 종료되었습니다"),
    WS_UNSUPPORTED_ACTION("WS-CMD-001", "지원하지 않는 타입의 액션입니다"),
    WS_UNSUPPORTED_FRAME("WS-FRAME-001", "지원하지 않는 타입의 프레임입니다"),
    WS_UNSUPPORTED_SYSTEM_ACTION("WS-SYSTEM-001", "지원하지 않는 시스템 액션입니다");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
