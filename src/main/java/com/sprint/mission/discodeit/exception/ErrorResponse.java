package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {
  Instant timeStamp;
  String code;
  String message;
  Map<String, Object> details;
  String exceptionType;
  int status;

  public ErrorResponse(ErrorResponse errorResponse) {
    this.timeStamp = errorResponse.getTimeStamp();
    this.code = errorResponse.getCode();
    this.message = errorResponse.getMessage();
    this.details = errorResponse.getDetails();
    this.exceptionType = errorResponse.getExceptionType();
    this.status = errorResponse.getStatus();
  }

}
