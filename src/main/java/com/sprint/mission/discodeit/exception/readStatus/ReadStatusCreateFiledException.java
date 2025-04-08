package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadStatusCreateFiledException extends ReadStatusException{

  public ReadStatusCreateFiledException(UUID userId, UUID channelId) {
    super(ErrorCode.READ_STATUS_CREATE_FAILED,"User ID, Channel ID", userId, channelId);
    log.error("ReadStatus with userId " + userId + " and channelId " + channelId + " already exists");
  }
}
