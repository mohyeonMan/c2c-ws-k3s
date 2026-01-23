package com.c2c.ws.application.port.out.presence;

public interface NodePresencePort {
    
    void markNodeActive(String nodeId);

    void markNodeInactive(String nodeId);

}
