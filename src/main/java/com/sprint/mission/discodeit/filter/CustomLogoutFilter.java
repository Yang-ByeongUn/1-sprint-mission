package com.sprint.mission.discodeit.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomLogoutFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if(request.getRequestURL().equals("/api/auth/logout") && request.getMethod().equalsIgnoreCase("POST")) {
      //세션 무효화
      HttpSession session = request.getSession(false);
      if(session != null) {
        session.invalidate();
      }

      //securityContext 초기화
      SecurityContextHolder.clearContext();

      //CSRF 검사를 수행하지 않아 바로 종료
      response.setStatus(HttpServletResponse.SC_OK);
      return;
    }
    filterChain.doFilter(request, response);
  }
}
