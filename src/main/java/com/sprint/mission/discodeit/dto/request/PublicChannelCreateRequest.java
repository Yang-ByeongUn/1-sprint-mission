package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

@NotNull
public record PublicChannelCreateRequest(
    String name,
    String description
) {

}
