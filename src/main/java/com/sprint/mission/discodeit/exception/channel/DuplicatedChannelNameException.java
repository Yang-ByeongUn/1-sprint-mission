package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class DuplicatedChannelNameException extends ChannelException {

  public DuplicatedChannelNameException(String name) {
    super(ErrorCode.DUPLICATED_CHANNEL_NAME,"Channel Name", name);
  }
}
