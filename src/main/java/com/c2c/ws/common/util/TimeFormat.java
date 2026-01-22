package com.c2c.ws.common.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class TimeFormat {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").withZone(ZoneOffset.UTC);

    private TimeFormat() {
    }

    public static String format(Instant instant) {
        if (instant == null) {
            return null;
        }
        return FORMATTER.format(instant);
    }

    public static Instant parse(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Instant.from(FORMATTER.parse(value));
    }
}
