package com.c2c.ws.adapter.out.presence;

import com.c2c.ws.application.port.out.presence.SessionPresencePort;

public class RedisSessionPresenceAdapter implements SessionPresencePort {

    @Override
    public void markSessionActive(String userId) {
        // Implementation for marking session as active
    }

    @Override
    public void markSessionInactive(String userId) {
        // Implementation for marking session as inactive
    }
}