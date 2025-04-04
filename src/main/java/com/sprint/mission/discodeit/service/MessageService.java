package com.sprint.mission.discodeit.service;

<<<<<<< HEAD
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  MessageDto find(UUID messageId);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createdAt, Pageable pageable);

  MessageDto update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
=======
import com.sprint.mission.discodeit.entity.Message;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID authorID, UUID channelID, String text);

    Map<UUID, Message> getMessages();

    Optional<Message> getMessage(UUID uuid);

    Optional<Message> updateMessage(UUID uuid, String text);

    Optional<Message> deleteMessage(UUID uuid);
>>>>>>> 67d19b8276e693bf808f9d9f2844e6ad4539f06b
}
