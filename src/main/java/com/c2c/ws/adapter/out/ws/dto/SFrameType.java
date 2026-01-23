package com.c2c.ws.adapter.out.ws.dto;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum SFrameType {
    ACK,
    RESULT,
    SYSTEM,
    NOTIFY,
    MESSAGE,
    EVENT,
    ERROR,
    @JsonEnumDefaultValue
    UNKNOWN;

    @JsonCreator
    public static SFrameType from(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        try {
            return SFrameType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}
