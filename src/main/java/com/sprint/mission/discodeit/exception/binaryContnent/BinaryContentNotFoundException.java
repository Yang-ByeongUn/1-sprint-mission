package com.sprint.mission.discodeit.exception.binaryContnent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException(UUID binaryContentId) {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND,"Binary Content Id", binaryContentId);
    log.error("BinaryContent with id " + binaryContentId + " not found");
  }
}
