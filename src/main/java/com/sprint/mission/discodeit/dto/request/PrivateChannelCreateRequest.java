package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
@NotNull @NotBlank @Size(min = 2, max = 255)
public record PrivateChannelCreateRequest(
    List<UUID> participantIds
) {

}
