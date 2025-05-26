package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoleDto {
  UUID id;
  String name;

  public static RoleDto toRoleDto(UUID id, String name) {
    return RoleDto.builder().id(id).name(name).build();
  }
  public static RoleDto toRoleDto(Role role) {
    return RoleDto.builder().id(role.getId()).name(role.getName()).build();
  }

}
