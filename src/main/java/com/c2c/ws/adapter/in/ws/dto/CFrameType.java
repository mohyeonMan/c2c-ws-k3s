package com.c2c.ws.adapter.in.ws.dto;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum CFrameType {
    ACK,
    COMMAND,
    @JsonEnumDefaultValue
    UNKNOWN;

    @JsonCreator
    public static CFrameType from(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        try {
            return CFrameType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}
