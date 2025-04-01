package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
@NotNull
public record MessageCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
