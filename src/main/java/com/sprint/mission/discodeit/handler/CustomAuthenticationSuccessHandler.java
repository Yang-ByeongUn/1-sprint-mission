package com.sprint.mission.discodeit.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    User user = (User) authentication.getPrincipal();
    BinaryContentDto profile = null;
    if (user.getProfile() != null) {
      profile = new BinaryContentDto(user.getProfile().getId(), user.getProfile().getFileName(),
          user.getProfile().getSize(), user.getProfile().getContentType());
    }

    UserDto dto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), profile, user.getStatus().isOnline(), user.getRoles());

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    new ObjectMapper().writeValue(response.getOutputStream(), dto);
  }
}
