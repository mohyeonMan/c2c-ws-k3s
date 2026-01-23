package com.c2c.ws.adapter.out.ws.dto;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Status {
    SUCCESS,
    ERROR,
    DELIVERED,
    @JsonEnumDefaultValue
    UNKNOWN;

    @JsonCreator
    public static Status from(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        try {
            return Status.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}
