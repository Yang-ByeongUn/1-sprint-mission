package com.sprint.mission.discodeit.exception.role;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class RoleNotFoundException extends RoleException{

  public RoleNotFoundException() {
    super(ErrorCode.ROLE_NOT_FOUND);
  }

  public static RoleNotFoundException notFoundException(String roleName) {
    RoleNotFoundException exception = new RoleNotFoundException();
    exception.addDetail("roleName", roleName);
    return exception;
  }
}