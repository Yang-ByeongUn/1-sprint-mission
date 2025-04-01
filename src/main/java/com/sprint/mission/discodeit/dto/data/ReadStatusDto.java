package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
@NotNull
public record ReadStatusDto(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
