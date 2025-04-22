/*
package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
@Slf4j
@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String requestId = UUID.randomUUID().toString();
    MDC.put("requestId", requestId);
    MDC.put("requestMethod", request.getMethod());
    MDC.put("requestUrl", request.getRequestURI());

    response.setHeader("Discodeit-Request-ID", requestId);
    log.debug("Request started");

    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    MDC.remove("requestId");
    MDC.remove("requestMethod");
    MDC.remove("requestUrl");
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
*/
