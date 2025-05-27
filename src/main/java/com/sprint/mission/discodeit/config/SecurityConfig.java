package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.filter.CustomLogoutFilter;
import com.sprint.mission.discodeit.filter.CustomUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.handler.CustomAuthenticationFailureHandler;
import com.sprint.mission.discodeit.handler.CustomAuthenticationSuccessHandler;
import com.sprint.mission.discodeit.session.SessionRegistry;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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


@Configuration
@EnableWebSecurity //  Spring Security를 활성화하며, 내부적으로 WebSecurityConfigurerAdapter 없이도 커스터마이징이 가능하게 설정합니다.
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
  private final UserDetailsService userDetailsService;
  private final DataSource dataSource;
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AuthenticationManager authManager,
      SecurityContextRepository contextRepo,
      SessionRegistry sessionRegistry) throws Exception {

    CustomUsernamePasswordAuthenticationFilter loginFilter =
        new CustomUsernamePasswordAuthenticationFilter(authManager, sessionRegistry);
    loginFilter.setSecurityContextRepository(contextRepo);

    http
        .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/csrf-token", "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/users/role").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/channels/public/**").hasRole("CHANNEL_MANAGER")
            .requestMatchers(HttpMethod.PUT, "/api/channels/public/**").hasRole("CHANNEL_MANAGER")
            .requestMatchers(HttpMethod.DELETE, "/api/channels/public/**").hasRole("CHANNEL_MANAGER")
            .requestMatchers("/api/**").hasRole("USER")
            .anyRequest().permitAll()
        )
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

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
    CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager, sessionRegistry);
    filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
    filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
    return filter;
  }


}
