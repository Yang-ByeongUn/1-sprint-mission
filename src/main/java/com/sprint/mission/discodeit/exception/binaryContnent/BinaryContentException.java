package com.sprint.mission.discodeit.exception.binaryContnent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class BinaryContentException extends DiscodeitException {

  public BinaryContentException(ErrorCode errorCode,String errorPosition, UUID binaryContentId) {
    super(errorCode, errorPosition, binaryContentId);
  }
}
