package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSessionRepository extends JpaRepository<JwtSession, Long> {
  Optional<JwtSession> findByAccessToken(String accessToken);
  Optional<JwtSession> findByRefreshToken(String refreshToken);
  void deleteByUser(User user);

  List<JwtSession> findAllByUserId(UUID userId);
}
