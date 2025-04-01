package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class MessageException extends DiscodeitException {

  public MessageException(ErrorCode errorCode,String errorPosition, UUID messageId) {
    super(errorCode,errorPosition, messageId);
  }
}
