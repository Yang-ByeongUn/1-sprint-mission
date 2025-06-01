package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.RoleRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  public void init() {
    // ROLE_ADMIN 없으면 생성
    Role adminRole = roleRepository.findByName("ROLE_ADMIN")
        .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

    // admin 계정 없으면 생성
    String adminUsername = "admin";
    if (!userRepository.existsByUsername(adminUsername)) {
      User admin = new User(
          adminUsername,
          "admin@example.com",
          passwordEncoder.encode("1234"),  // 기본 비밀번호
          null  // 프로필은 없음
      );
      admin.setRoles(Set.of(adminRole));
      userRepository.save(admin);
    }
  }
}
