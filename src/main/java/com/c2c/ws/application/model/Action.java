package com.c2c.ws.application.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Action {
    CONN_CLOSED,
    ROOM_CREATE,
    JOIN,
    JOIN_REQUEST,
    JOIN_APPROVE,
    JOIN_REJECT,
    LEAVE,
    CLIENT_MESSAGE,
    HEARTBEAT,
    UNKNOWN;
}
