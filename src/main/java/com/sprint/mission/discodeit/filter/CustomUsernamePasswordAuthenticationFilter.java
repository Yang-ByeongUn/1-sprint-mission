package com.sprint.mission.discodeit.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.LoginFailException;
import com.sprint.mission.discodeit.session.SessionRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final SessionRegistry sessionRegistry;

  public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
      SessionRegistry sessionRegistry) {
    super.setAuthenticationManager(authenticationManager);
    this.sessionRegistry = sessionRegistry;
    setFilterProcessesUrl("/api/auth/login");
  }
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
      return this.getAuthenticationManager().authenticate(authRequest);

    } catch (IOException e) {
      throw new AuthenticationServiceException("Invaild login payload", e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
      throws IOException, ServletException {
    User user = (User) authResult.getPrincipal();
    UserDto dto = new UserDto(user.getId(), user.getUsername(), user.getEmail(),
        new BinaryContentDto(user.getProfile().getId(), user.getProfile().getFileName(), user.getProfile().getSize(),
            user.getProfile().getContentType()), user.getStatus().isOnline(), user.getRoles());

    sessionRegistry.register(user.getId(), request.getSession(false));

    response.setContentType("application/json");
    new ObjectMapper().writeValue(response.getOutputStream(), dto);
  }
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    LoginFailException error = new LoginFailException();
    new ObjectMapper().writeValue(response.getOutputStream(), error);
  }
}
