package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
@NotNull
public record UserStatusCreateRequest(
    UUID userId,
    Instant lastActiveAt
) {

}
