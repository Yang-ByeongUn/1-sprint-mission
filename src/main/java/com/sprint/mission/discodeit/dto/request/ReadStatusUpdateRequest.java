package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
@NotNull
public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
