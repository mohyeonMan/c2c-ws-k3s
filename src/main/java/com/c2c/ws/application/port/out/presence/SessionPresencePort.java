package com.c2c.ws.application.port.out.presence;

public interface SessionPresencePort {

    void markSessionActive(String userId, String nodeQueue);

    void markSessionInactive(String userId);
    
}
