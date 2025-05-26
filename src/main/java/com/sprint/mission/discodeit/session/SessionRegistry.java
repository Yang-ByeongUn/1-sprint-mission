package com.sprint.mission.discodeit.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {

  private final Map<UUID, HttpSession> sessions = new ConcurrentHashMap<>();

  public void register(UUID userId, HttpSession session) {
    sessions.put(userId, session);
  }

  public void invalidate(UUID userId) {
    HttpSession session = sessions.remove(userId);
    if (session != null) {
      session.invalidate();
    }
  }
}
