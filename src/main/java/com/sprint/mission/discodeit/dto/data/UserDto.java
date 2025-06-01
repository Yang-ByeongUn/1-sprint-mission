package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Role;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online,
    Set<Role> roles
) {
  public Set<GrantedAuthority> authorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
        .collect(Collectors.toSet());
  }

}