package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException(String userInfo) {
    super(ErrorCode.USER_ALREADY_EXISTS,"Duplicated UserInfo", userInfo);
    log.error("User with username " + userInfo + " already exists");
  }

  public UserAlreadyExistsException(UUID userId) {
    super(ErrorCode.USER_ALREADY_EXISTS,"Duplicated User Status", userId);
    log.error("UserStatus with id " + userId + " already exists");
  }
}
