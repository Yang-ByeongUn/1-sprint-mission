package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtSessionService {
  private final ObjectMapper objectMapper;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;
  @Value("${jwt.secret}")
  private String secret;
  private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15;
  private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7;

  public Optional<JwtSession> findByRefreshToken(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken);
  }

  public boolean isValid(String refreshToken) {
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken).getBody();
    Date expiration = claims.getExpiration();
    if(expiration.before(new Date())) {
      return false;
    }
    return jwtSessionRepository.findByRefreshToken(refreshToken).isPresent();
  }

  public String createAccessToken(UUID userId) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY); // 예: 15분

    return Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  public int getRefreshTokenExpirySeconds() {
    return (int) (REFRESH_TOKEN_VALIDITY / 1000);
  }

  public record TokenPair(String accessToken, String refreshToken) {}

  @Transactional
  public TokenPair createTokens(UserDto userDto){
    long now = System.currentTimeMillis();

    String accessToken = createToken(userDto, now, now + ACCESS_TOKEN_VALIDITY);
    String refreshToken = createToken(userDto, now, now + REFRESH_TOKEN_VALIDITY);
    User user = userRepository.findById(userDto.id())
        .orElseThrow(UserNotFoundException::new);

    jwtSessionRepository.deleteByUser(user); // 기존 세션 제거
    JwtSession session = JwtSession.builder()
        .user(user)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .createdAt(now)
        .expiresAt(now + REFRESH_TOKEN_VALIDITY)
        .build();
    jwtSessionRepository.save(session);
    return new TokenPair(accessToken, refreshToken);
  }

  private String createToken(UserDto userDto, long issuedAt, long expiration) {
    try {
      return Jwts.builder()
          .claim("userDto", objectMapper.writeValueAsString(userDto))
          .claim("iat", issuedAt)
          .claim("exp", expiration)
          .setIssuedAt(new Date(issuedAt))
          .setExpiration(new Date(expiration))
          .signWith(SignatureAlgorithm.HS256, secret)
          .compact();
    } catch (JsonProcessingException e) {
      throw new RuntimeException("JWT 생성 실패", e);
    }
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  //리프레시 토큰 사용 해 엑세스 토큰을 재발급
  @Transactional
  public TokenPair reissueToken(String refreshToken) {
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken).orElseThrow(IllegalStateException::new);
    UserDto userDto = extractUserDto(refreshToken);
    return createTokens(userDto);
  }
  //JWT의 서명 검증 및 유효성 검사
  public UserDto extractUserDto(String refreshToken) {
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken).getBody();
    try {
      //역직렬화를 통해 값 추출 JSON -> STRING
      String json = claims.get("userDto", String.class);
      return objectMapper.readValue(json, UserDto.class);
    } catch (Exception e) {
      throw new RuntimeException("userDto 추출 실패", e);
    }
  }

  @Transactional
  public void invalidateRefreshToken(String refreshToken) {
    jwtSessionRepository.findByRefreshToken(refreshToken)
        .ifPresent(jwtSessionRepository::delete);
  }

  @Transactional
  public void updateUserRole(UUID userId, Set<Role> newRole) {
    // 권한 변경
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    user.update(null, null, null, null, newRole);
    userRepository.save(user);

    // 로그인 중이라면 강제 로그아웃 (refreshToken 무효화)
    List<JwtSession> sessions = jwtSessionRepository.findAllByUserId(userId);
    jwtSessionRepository.deleteAll(sessions);
  }


}
