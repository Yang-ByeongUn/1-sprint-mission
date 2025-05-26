package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadStatusNotFoundException  extends ReadStatusException{

  public ReadStatusNotFoundException(UUID readStatusId) {
    super(ErrorCode.READ_STATUS_NOT_FOUND,"Read Status Id", readStatusId);
    log.error("ReadStatus with id " + readStatusId + " not found");
  }
}
