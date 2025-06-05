package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.RoleDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionService;
import com.sprint.mission.discodeit.security.jwt.JwtSessionService.TokenPair;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final UserService userService;
  private final JwtSessionService jwtSessionService;
  private final AuthenticationManager authenticationManager;

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    log.info("CSRF 토큰 발급");
    return ResponseEntity.ok(csrfToken);
  }

  //현재 로그인된 사용자의 정보를 반환하는 API입니다.
  @GetMapping("/me")
  public ResponseEntity<String> me(@CookieValue(value = "refresh_token", required = false) String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰 없음");
    }
    return jwtSessionService.findByRefreshToken(refreshToken).map(JwtSession::getAccessToken).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰"));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response,
      @CookieValue(value = "refresh_token", required = false) String refreshToken) {
    if (refreshToken == null || !jwtSessionService.isValid(refreshToken)) {

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    jwtSessionService.invalidateRefreshToken(refreshToken);
    Cookie deleteCookie = new Cookie("refresh_token", null);
    deleteCookie.setHttpOnly(true);
    deleteCookie.setPath("/");
    deleteCookie.setMaxAge(0);
    response.addCookie(deleteCookie);
    return ResponseEntity.ok().build();
  }


  @PutMapping("/role")
  public ResponseEntity<UserDto> roleUpdate(@RequestBody UserRoleUpdateRequest request) {
    log.info("권한 변경 요청: userId={}, newRole={}", request.getUserId(), request.getNewRole());

    UserDto updatedUser = userService.updateUserRole(request.getUserId(), RoleDto.toRoleDto(request.getNewRole()));

    log.info("권한 변경 완료: userId={}, newRole={}", request.getUserId(), request.getNewRole());
    return ResponseEntity.ok(updatedUser);
  }

  @GetMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
    try {
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      UserDto userDto = (UserDto) authentication.getPrincipal();
      TokenPair tokens = jwtSessionService.createTokens(userDto);

      Cookie refreshTokenCookie = new Cookie("refresh_token", tokens.refreshToken());
      refreshTokenCookie.setHttpOnly(true);
      refreshTokenCookie.setPath("/"); // 필요시 도메인 설정
      refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
      response.addCookie(refreshTokenCookie);
      return ResponseEntity.ok(tokens.accessToken());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@RequestHeader("refresh_token") String refreshToken, HttpServletResponse response) {
    if(refreshToken == null || refreshToken.isBlank()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
    Optional<JwtSession> sessionOptional = jwtSessionService.findByRefreshToken(refreshToken);
    if(sessionOptional.isEmpty()){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session not found");
    }
    JwtSession session = sessionOptional.get();
    UUID userId = session.getUser().getId();

    String newAccessToken = jwtSessionService.createAccessToken(userId);
    Cookie refreshTokenCookie = new Cookie("refresh_token", newAccessToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setMaxAge(jwtSessionService.getRefreshTokenExpirySeconds());
    response.addCookie(refreshTokenCookie);
    return ResponseEntity.ok(newAccessToken);

  }
}