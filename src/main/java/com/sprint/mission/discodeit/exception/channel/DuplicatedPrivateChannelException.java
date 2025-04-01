package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplicatedPrivateChannelException extends ChannelException {

  public DuplicatedPrivateChannelException(List<UUID> uuids) {
    super(ErrorCode.DUPLICATED_PRIVATE_CHANNEL_USERS,"Duplicated Channel Users", uuids);
    log.error("DuplicatedPrivateChannelException : " + uuids);
  }
}
