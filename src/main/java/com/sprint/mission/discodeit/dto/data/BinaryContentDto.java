package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
@NotNull
public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

}
