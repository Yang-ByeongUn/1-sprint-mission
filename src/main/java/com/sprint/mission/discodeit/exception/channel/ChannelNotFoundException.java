package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelNotFoundException extends ChannelException{

  public ChannelNotFoundException(UUID channelId) {
    super(ErrorCode.CHANNEL_NOT_FOUND,"Channel Name", channelId);
    log.error("Channel with id " + channelId + " not found");
  }
}
