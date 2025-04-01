package com.sprint.mission.discodeit.exception.binaryContnent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class BinaryContentStorageFileNotFoundException extends BinaryContentException {

  public BinaryContentStorageFileNotFoundException(UUID binaryContentId) {
    super(ErrorCode.BINARY_CONTENT_STORAGE_FILE_NOT_FOUND,"File Name", binaryContentId);
  }
}
