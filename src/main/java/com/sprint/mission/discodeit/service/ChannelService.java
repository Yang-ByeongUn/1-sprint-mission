package com.sprint.mission.discodeit.service;

<<<<<<< HEAD
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto create(PublicChannelCreateRequest request);

  ChannelDto create(PrivateChannelCreateRequest request);

  ChannelDto find(UUID channelId);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto update(UUID channelId, PublicChannelUpdateRequest request);

  void delete(UUID channelId);
}
=======
import com.sprint.mission.discodeit.entity.Channel;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String channelName);

    Map<UUID, Channel> getChannels();

    Optional<Channel> getChannel(UUID uuid);

    Optional<Channel> addMessageToChannel(UUID channelUUID, UUID messageUUID);

    Optional<Channel> updateChannel(UUID uuid, String channelName);

    Optional<Channel> deleteChannel(UUID uuid);
}
>>>>>>> 67d19b8276e693bf808f9d9f2844e6ad4539f06b
