package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(UUID messageId) {
    super(ErrorCode.MESSAGE_NOT_FOUND,"Message Id", messageId);
    log.error("Message with id " + messageId + " not found");
  }
}
