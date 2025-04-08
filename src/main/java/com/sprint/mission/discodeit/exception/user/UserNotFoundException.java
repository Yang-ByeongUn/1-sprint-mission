package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException  extends UserException{

  public UserNotFoundException(UUID userId) {
    super(ErrorCode.USER_NOT_FOUND,"User Id", userId);
    log.error("User with id " + userId + " not found");
  }
}
