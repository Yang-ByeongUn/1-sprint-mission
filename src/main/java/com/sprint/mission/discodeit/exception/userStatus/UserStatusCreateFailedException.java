package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserStatusCreateFailedException extends UserStatusException {

  public UserStatusCreateFailedException(UUID userId) {
    super(ErrorCode.USER_STATUS_CREATE_FAILED,"User Id", userId);
    log.error("UserStatus with userId " + userId + " not found");
  }
}
