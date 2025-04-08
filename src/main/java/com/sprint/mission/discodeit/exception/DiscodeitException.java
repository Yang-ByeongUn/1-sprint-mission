package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timeStamp;
  private final ErrorCode errorCode;
  private final Map<String, Object> details = new HashMap<>(); //예외 발생 상황에 대한 추가 정보 저장

  public DiscodeitException(ErrorCode errorCode,String errorPosition,  Object object) {
    super(errorCode.getMessage());
    this.timeStamp = Instant.now();
    this.errorCode = errorCode;
    details.put(errorPosition, object);
  }

}
