package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@NotNull
public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {


  public static BinaryContentCreateRequest create(MultipartFile file) throws IOException {
    return new BinaryContentCreateRequest(file.getOriginalFilename(), file.getContentType(), file.getBytes());
  }
}
