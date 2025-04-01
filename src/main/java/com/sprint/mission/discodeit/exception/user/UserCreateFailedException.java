package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserCreateFailedException extends UserException {
  public UserCreateFailedException(String userInfo) {
    super(ErrorCode.USER_CREATE_FAILED,"User Name Or Email", userInfo);
    log.error("User with username or email " + userInfo + " already exists");
  }

}
