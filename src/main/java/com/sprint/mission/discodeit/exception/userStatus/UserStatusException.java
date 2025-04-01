package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class UserStatusException  extends DiscodeitException {

  public UserStatusException(ErrorCode errorCode,String errorPosition, UUID userId) {
    super(errorCode,errorPosition, userId);
  }
}
