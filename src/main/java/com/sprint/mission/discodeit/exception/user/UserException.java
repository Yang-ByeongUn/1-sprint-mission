package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.UUID;

public class UserException  extends DiscodeitException {

  public UserException(ErrorCode errorCode,String errorPosition, UUID userId) {
    super(errorCode,errorPosition, userId);
  }

  public UserException(ErrorCode errorCode,String errorPosition, String userInfo) {
    super(errorCode,errorPosition, userInfo);
  }
}
