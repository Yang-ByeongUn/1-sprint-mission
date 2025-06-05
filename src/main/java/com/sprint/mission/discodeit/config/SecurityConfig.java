package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.filter.CustomLogoutFilter;
import com.sprint.mission.discodeit.filter.CustomUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.filter.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.handler.CustomAuthenticationFailureHandler;
import com.sprint.mission.discodeit.handler.CustomAuthenticationSuccessHandler;
import com.sprint.mission.discodeit.security.jwt.JwtSessionService;
import com.sprint.mission.discodeit.session.SessionRegistry;
import java.util.List;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;



@Configuration
@EnableWebSecurity //  Spring Security를 활성화하며, 내부적으로 WebSecurityConfigurerAdapter 없이도 커스터마이징이 가능하게 설정합니다.
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
  private final UserDetailsService userDetailsService;
  private final DataSource dataSource;
  private final JwtSessionService jwtSessionService;

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AuthenticationManager authManager,
      SecurityContextRepository contextRepo,
      SessionRegistry sessionRegistry) throws Exception {

    CustomUsernamePasswordAuthenticationFilter loginFilter =
        new CustomUsernamePasswordAuthenticationFilter(authManager, sessionRegistry, objectMapper());
    loginFilter.setSecurityContextRepository(contextRepo);

    //AntPathRequestMatcher 키워드
    List<RequestMatcher> publicMatchers = List.of(
        new AntPathRequestMatcher("/api/auth/csrf-token", HttpMethod.GET.name()),
        new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name()),
        new AntPathRequestMatcher("/api/users", HttpMethod.POST.name())
    );

    List<RequestMatcher> adminMatchers = List.of(
        new AntPathRequestMatcher("/api/users/role", HttpMethod.PUT.name())
    );

    List<RequestMatcher> channelManagerMatchers = List.of(
        new AntPathRequestMatcher("/api/channels/public/**", HttpMethod.POST.name()),
        new AntPathRequestMatcher("/api/channels/public/**", HttpMethod.PUT.name()),
        new AntPathRequestMatcher("/api/channels/public/**", HttpMethod.DELETE.name())
    );

    RequestMatcher userMatcher = new AntPathRequestMatcher("/api/**");

    http
        .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .authorizeHttpRequests(auth -> {
          publicMatchers.forEach(matcher -> auth.requestMatchers(matcher).permitAll());
          adminMatchers.forEach(matcher -> auth.requestMatchers(matcher).hasRole("ADMIN"));
          channelManagerMatchers.forEach(matcher -> auth.requestMatchers(matcher).hasRole("CHANNEL_MANAGER"));
          auth.requestMatchers(userMatcher).hasRole("USER");
          auth.anyRequest().permitAll();
        })


        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

    http.addFilterBefore(new JwtAuthenticationFilter(jwtSessionService),
        UsernamePasswordAuthenticationFilter.class);


    return http.build();
  }


  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
    repo.setDataSource(dataSource);
    return repo;
  }
  @Bean
  public RememberMeServices rememberMeServices() {
    PersistentTokenBasedRememberMeServices service =
        new PersistentTokenBasedRememberMeServices("remember-me", userDetailsService,
            persistentTokenRepository());
    service.setTokenValiditySeconds(60 * 60 * 24 * 21);
    service.setAlwaysRemember(false);
    return service;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    //Security 인증 시 롤 계층 구조가 적용
    provider.setAuthoritiesMapper(new RoleHierarchyAuthoritiesMapper(roleHierarchy()));
    provider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(provider);
  }

  @Bean
  public CustomLogoutFilter customLogoutFilter() {
    return new CustomLogoutFilter();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(new BCryptPasswordEncoder());
    return provider;
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy("""
            ROLE_ADMIN > ROLE_CHANNEL_MANAGER
            ROLE_CHANNEL_MANAGER > ROLE_USER
        """);
    return hierarchy;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
      SessionRegistry sessionRegistry) {
    CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager, sessionRegistry, objectMapper());

    filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
    filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
    return filter;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }


}
