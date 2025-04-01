package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
@NotNull
public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online
) {

}
