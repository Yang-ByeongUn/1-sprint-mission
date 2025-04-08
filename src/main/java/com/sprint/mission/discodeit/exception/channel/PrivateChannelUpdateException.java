package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException(UUID channelID) {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE,"Private Channel Update Error", channelID);
    log.error("Private channel ("+channelID+") cannot be updated");
  }
}
