package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class LoginFailException extends UserException{
  public LoginFailException() {
    super(ErrorCode.LOGIN_FAIL);
  }


  public static LoginFailException loginFailException() {
    return new LoginFailException();
  }
}
