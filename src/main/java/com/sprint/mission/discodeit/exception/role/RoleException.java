package com.sprint.mission.discodeit.exception.role;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class RoleException extends DiscodeitException {
    public RoleException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RoleException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 