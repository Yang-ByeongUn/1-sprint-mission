package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ReadStatusException extends DiscodeitException {

  public ReadStatusException(ErrorCode errorCode,String errorPosition, UUID readStatusId) {
    super(errorCode,errorPosition, readStatusId);
  }

  public ReadStatusException(ErrorCode errorCode,String errorPosition, UUID userId, UUID channelId) {
    super(errorCode,errorPosition,  userId.toString() +","+ channelId.toString());
  }
}
