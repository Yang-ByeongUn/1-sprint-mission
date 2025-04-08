package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
@NotNull
public record UserStatusDto(
    UUID id,
    UUID userId,
    Instant lastActiveAt) {

}
