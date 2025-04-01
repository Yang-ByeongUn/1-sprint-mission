package com.sprint.mission.discodeit.exception.binaryContnent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class BinaryContentSameFileExistsException extends BinaryContentException {

  public BinaryContentSameFileExistsException(UUID binaryContentId) {
    super(ErrorCode.BINARY_CONTENT_SAME_FILE_EXISTS,"File Name",  binaryContentId);
  }
}
