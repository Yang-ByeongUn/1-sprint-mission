package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChannelException extends DiscodeitException {

  public ChannelException(ErrorCode errorCode, String errorPosition, UUID channelId) {
    super(errorCode, errorPosition, channelId);
  }

  public ChannelException(ErrorCode errorCode, String errorPosition, String name) {
    super(errorCode, errorPosition, name);
  }

  public ChannelException(ErrorCode errorCode, String errorPosition, List<UUID> uuids) {
    super(errorCode, errorPosition, uuids);
  }
}
