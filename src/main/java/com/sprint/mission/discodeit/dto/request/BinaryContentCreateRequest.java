package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

@NotNull
public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
